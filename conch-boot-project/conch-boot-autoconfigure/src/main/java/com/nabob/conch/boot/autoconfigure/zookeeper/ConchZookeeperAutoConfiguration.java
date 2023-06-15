package com.nabob.conch.boot.autoconfigure.zookeeper;

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
@EnableConfigurationProperties(ConchZookeeperProperties.class)
@ConditionalOnClass(CuratorFramework.class)
@ConditionalOnProperty(value = "conch.zk.enable", havingValue = "true", matchIfMissing = false)
public class ConchZookeeperAutoConfiguration {

    @Resource
    private ConchZookeeperProperties conchZookeeperProperties;

    @Bean(name = "curatorFramework")
    @ConditionalOnMissingBean(name = "curatorFramework")
    protected CuratorFramework curatorFramework() throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(conchZookeeperProperties.getRetryPolicy().getBaseSleepTime(),
                conchZookeeperProperties.getRetryPolicy().getRetryNum(),
                conchZookeeperProperties.getRetryPolicy().getMaxSleepTime());

        return CuratorFrameworkFactory.builder()
                .connectString(conchZookeeperProperties.getZhosts())
                .sessionTimeoutMs(conchZookeeperProperties.getSessionTimeout())
//                .connectionTimeoutMs(conchZookeeperProperties.getConnectionTimeout())
                .namespace(conchZookeeperProperties.getNamespace())
                .retryPolicy(retryPolicy)
                .build();
    }

}
