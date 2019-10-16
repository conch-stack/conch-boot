package ltd.beihu.core.boot.autoconfigure.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: zjz
 * @Desc: 自动配置类
 * @Date: 18-6-7
 * @Version: V1.0.0
 */
@Configuration
@EnableConfigurationProperties(BeihuZookeeperProperties.class)
@ConditionalOnClass(CuratorFramework.class)
@ConditionalOnProperty(value = "beihu.zk.enable", havingValue = "true", matchIfMissing = false)
public class BeihuZookeeperAutoConfiguration {

    @Resource
    private BeihuZookeeperProperties beihuZookeeperProperties;

    @Bean(name = "curatorFramework")
    @ConditionalOnMissingBean(name = "curatorFramework")
    protected CuratorFramework curatorFramework() throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(beihuZookeeperProperties.getRetryPolicy().getBaseSleepTime(),
                beihuZookeeperProperties.getRetryPolicy().getRetryNum(),
                beihuZookeeperProperties.getRetryPolicy().getMaxSleepTime());

        return CuratorFrameworkFactory.builder()
                .connectString(beihuZookeeperProperties.getZhosts())
                .sessionTimeoutMs(beihuZookeeperProperties.getSessionTimeout())
//                .connectionTimeoutMs(beihuZookeeperProperties.getConnectionTimeout())
                .namespace(beihuZookeeperProperties.getNamespace())
                .retryPolicy(retryPolicy)
                .build();
    }

}
