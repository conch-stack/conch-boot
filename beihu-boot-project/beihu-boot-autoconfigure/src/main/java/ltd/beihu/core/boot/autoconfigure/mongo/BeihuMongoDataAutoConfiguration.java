/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ltd.beihu.core.boot.autoconfigure.mongo;

import com.mongodb.ClientSessionOptions;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import ltc.beihu.core.mongo.boot.dao.GridDao;
import ltc.beihu.core.mongo.boot.dao.impl.GridDaoImpl;
import ltd.beihu.core.boot.autoconfigure.mongo.BeihuMongoDataAutoConfiguration.AnyMongoClientAvailable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoDbFactorySupport;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's mongo support.
 * <p>
 * Registers a {@link MongoTemplate} and {@link GridFsTemplate} beans if no other beans of
 * the same type are configured.
 * <P>
 * Honors the {@literal spring.data.mongodb.database} property if set, otherwise connects
 * to the {@literal test} database.
 *
 * @author Dave Syer
 * @author Oliver Gierke
 * @author Josh Long
 * @author Phillip Webb
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Christoph Strobl
 * @since 1.1.0
 */
@Configuration
@ConditionalOnClass({ MongoClient.class, com.mongodb.client.MongoClient.class,
		MongoTemplate.class })
@Conditional(AnyMongoClientAvailable.class)
@EnableConfigurationProperties(BeihuMongoProperties.class)
@AutoConfigureAfter(BeihuMongoAutoConfiguration.class)
public class BeihuMongoDataAutoConfiguration {

	private final ApplicationContext applicationContext;

	private final BeihuMongoProperties beihuMongoProperties;

	public BeihuMongoDataAutoConfiguration(ApplicationContext applicationContext,
										   BeihuMongoProperties beihuMongoProperties) {
		this.applicationContext = applicationContext;
		this.beihuMongoProperties = beihuMongoProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public MongoMappingContext mongoMappingContext(MongoCustomConversions conversions)
			throws ClassNotFoundException {
		MongoMappingContext context = new MongoMappingContext();
		context.setInitialEntitySet(new EntityScanner(this.applicationContext)
				.scan(Document.class, Persistent.class));
		Class<?> strategyClass = this.beihuMongoProperties.getFieldNamingStrategy();
		if (strategyClass != null) {
			context.setFieldNamingStrategy(
					(FieldNamingStrategy) BeanUtils.instantiateClass(strategyClass));
		}
		context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
		return context;
	}

	@Bean
	@ConditionalOnMissingBean
	public MongoCustomConversions mongoCustomConversions() {
		return new MongoCustomConversions(Collections.emptyList());
	}

	@Bean
	@ConditionalOnMissingBean(MongoDbFactory.class)
	public MongoDbFactorySupport<?> mongoDbFactory(ObjectProvider<MongoClient> mongo,
			ObjectProvider<com.mongodb.client.MongoClient> mongoClient) {
		MongoClient preferredClient = mongo.getIfAvailable();
		if (preferredClient != null) {
			return new SimpleMongoDbFactory(preferredClient,
					this.beihuMongoProperties.getMongoClientDatabase());
		}
		com.mongodb.client.MongoClient fallbackClient = mongoClient.getIfAvailable();
		if (fallbackClient != null) {
			return new SimpleMongoClientDbFactory(fallbackClient,
					this.beihuMongoProperties.getMongoClientDatabase());
		}
		throw new IllegalStateException("Expected to find at least one MongoDB client.");
	}

	@Bean
	@ConditionalOnMissingBean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory,
			MongoConverter converter) {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);
		if (this.beihuMongoProperties.getEnv().startsWith("pro")){
			mongoTemplate.setReadPreference(ReadPreference.secondary()); // 配置读写分离(从节点读)生产环境打开
		}
		return mongoTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(MongoConverter.class)
	public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory,
			MongoMappingContext context, MongoCustomConversions conversions) {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
		MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver,
				context);
		// 去掉 _class项
		mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		mappingConverter.setCustomConversions(conversions);
		return mappingConverter;
	}

	@Bean
	@ConditionalOnMissingBean
	public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory,
			MongoTemplate mongoTemplate) {
		return new GridFsTemplate(
				new GridFsMongoDbFactory(mongoDbFactory, this.beihuMongoProperties),
				mongoTemplate.getConverter());
	}

	/**
	 * 文件操作接口
	 */
	@Bean
	@ConditionalOnMissingBean
	public GridDao gridDao(GridFsTemplate gridFsTemplate) {
		return new GridDaoImpl(gridFsTemplate);
	}

	/**
	 * {@link MongoDbFactory} decorator to respect
	 * {@link MongoProperties#getGridFsDatabase()} if set.
	 */
	private static class GridFsMongoDbFactory implements MongoDbFactory {

		private final MongoDbFactory mongoDbFactory;

		private final BeihuMongoProperties beihuMongoProperties;

		GridFsMongoDbFactory(MongoDbFactory mongoDbFactory, BeihuMongoProperties beihuMongoProperties) {
			Assert.notNull(mongoDbFactory, "MongoDbFactory must not be null");
			Assert.notNull(beihuMongoProperties, "Properties must not be null");
			this.mongoDbFactory = mongoDbFactory;
			this.beihuMongoProperties = beihuMongoProperties;
		}

		@Override
		public MongoDatabase getDb() throws DataAccessException {
			String gridFsDatabase = this.beihuMongoProperties.getGridFsDatabase();
			if (StringUtils.hasText(gridFsDatabase)) {
				return this.mongoDbFactory.getDb(gridFsDatabase);
			}
			return this.mongoDbFactory.getDb();
		}

		@Override
		public MongoDatabase getDb(String dbName) throws DataAccessException {
			return this.mongoDbFactory.getDb(dbName);
		}

		@Override
		public PersistenceExceptionTranslator getExceptionTranslator() {
			return this.mongoDbFactory.getExceptionTranslator();
		}

		@Override
		@Deprecated
		public DB getLegacyDb() {
			return this.mongoDbFactory.getLegacyDb();
		}

		@Override
		public ClientSession getSession(ClientSessionOptions options) {
			return this.mongoDbFactory.getSession(options);
		}

		@Override
		public MongoDbFactory withSession(ClientSession session) {
			return this.mongoDbFactory.withSession(session);
		}

	}

	/**
	 * Check if either a {@link MongoClient com.mongodb.MongoClient} or
	 * {@link com.mongodb.client.MongoClient com.mongodb.client.MongoClient} bean is
	 * available.
	 */
	public static class AnyMongoClientAvailable extends AnyNestedCondition {

		AnyMongoClientAvailable() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnBean(MongoClient.class)
		static class PreferredClientAvailable {

		}

		@ConditionalOnBean(com.mongodb.client.MongoClient.class)
		static class FallbackClientAvailable {

		}

	}
}
