package com.nabob.conch.boot.autoconfigure.sqlinit;

import com.gitee.hengboy.mybatis.enhance.MapperFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * SQL Init Auto Configuration
 * <p>
 * Only Support For MySQL or MariaDB
 *
 * @author Adam
 * @since 2023/6/15
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MapperFactoryBean.class})
@ConditionalOnBean({DataSource.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@ConditionalOnProperty(value = "conch.init-sql.enable", havingValue = "true")
public class ConchSqlInitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SqlFileInitializer sqlFileInitializer(ObjectProvider<JdbcTemplate> jdbcTemplateObjectProvider,
                                                 ObjectProvider<Environment> environmentObjectProvider,
                                                 ObjectProvider<SqlSessionFactory> sqlSessionFactoryObjectProvider) {
        SqlExecutor sqlExecutor = new SqlExecutor(jdbcTemplateObjectProvider.getIfAvailable());
        return new SqlFileInitializer(environmentObjectProvider.getIfAvailable(),
                sqlExecutor, sqlSessionFactoryObjectProvider.getIfAvailable());
    }

}
