package ltd.beihu.core.boot.autoconfigure.mutiles;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

import java.util.Map;
import java.util.Properties;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/11/14
 * @Version: V1.0.0
 */
@Configuration
@ConditionalOnClass({Client.class, TransportClientFactoryBean.class, ElasticsearchTemplate.class})
@EnableConfigurationProperties({BeihuMutilElasticsearchProperties.class})
@ConditionalOnProperty(value = "beihu.data.elasticsearch.mutil.transport.enable", havingValue = "true", matchIfMissing = false)
public class BeihuMutilElasticsearchAutoConfiguration {

    private final BeihuMutilElasticsearchProperties properties;
    private ApplicationContext applicationContext;

    public BeihuMutilElasticsearchAutoConfiguration(BeihuMutilElasticsearchProperties properties, ApplicationContext applicationContext) throws Exception {
        this.properties = properties;
        this.applicationContext = applicationContext;
        init();
    }

    private void init() throws Exception{
        Map<String, BeihuMutilElasticsearchProperties.Instances> instances = this.properties.instances;
        for(Map.Entry<String, BeihuMutilElasticsearchProperties.Instances> entry : instances.entrySet()) {
            String connectionName = entry.getKey();
            BeihuMutilElasticsearchProperties.Instances instance = entry.getValue();
            // connectionName -> ElasticsearchTemplate(TransportClient)
            registerBean(connectionName, elasticsearchTemplate(elasticsearchClient(instance)));
        }
    }

    public ElasticsearchTemplate elasticsearchTemplate(Client client) {
        try {
            SimpleElasticsearchMappingContext simpleElasticsearchMappingContext = new SimpleElasticsearchMappingContext();
            ElasticsearchConverter elasticsearchConverter = new MappingElasticsearchConverter(simpleElasticsearchMappingContext);
            return new ElasticsearchTemplate(client, elasticsearchConverter);
        } catch (Exception var4) {
            throw new IllegalStateException(var4);
        }
    }

    public TransportClient elasticsearchClient(BeihuMutilElasticsearchProperties.Instances instance) throws Exception {
        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        factory.setClusterNodes(instance.getClusterNodes());
        factory.setProperties(this.createProperties(instance));
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    private Properties createProperties(BeihuMutilElasticsearchProperties.Instances instance) {
        Properties properties = new Properties();
        properties.put("cluster.name", instance.getClusterName());
        properties.putAll(instance.getProperties());
        return properties;
    }

    private void registerBean(String name, Object obj) {
        // 获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext
                .getAutowireCapableBeanFactory();
        // 动态注册bean.
        defaultListableBeanFactory.registerSingleton(name, obj);
    }
}
