package com.nabob.conch.boot.autoconfigure.antiscrapy;

import com.nabob.conch.redis.RedisGenericUtil;
import com.nabob.conch.antiscrapy.AntiSecurityService;
import com.nabob.conch.antiscrapy.common.SecurityConstants;
import com.nabob.conch.antiscrapy.handler.RedisStrategyHandler;
import com.nabob.conch.antiscrapy.handler.StrategyHandler;
import com.nabob.conch.antiscrapy.strategy.SecurityStrategyFactory;
import com.nabob.conch.boot.autoconfigure.antiscrapy.strategy.IpAndUaSecurityStrategy;
import com.nabob.conch.boot.autoconfigure.antiscrapy.strategy.IpSecurityStrategy;
import com.nabob.conch.boot.autoconfigure.antiscrapy.strategy.PhoneSecurityStrategy;
import com.nabob.conch.boot.autoconfigure.redis.ConchRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zjz
 * @Desc: 自动配置类
 * @Date: 18-6-7
 * @Version: V1.0.0
 */
@Configuration
@EnableConfigurationProperties(AntiSecurityProperties.class)
@ConditionalOnProperty(value = "conch.anti.security.strategy.enable", havingValue = "true", matchIfMissing = false)
@AutoConfigureAfter(ConchRedisAutoConfiguration.class)
public class AntiSecurityAutoConfiguration {

    // handler
    @Bean(name = SecurityConstants.strategyHandler)
    @ConditionalOnMissingBean(name = SecurityConstants.strategyHandler)
    public StrategyHandler strategyHandler(RedisGenericUtil redisGenericUtil) {
        return new RedisStrategyHandler(redisGenericUtil);
    }

    // strategy
    @Bean(name = SecurityConstants.ipSecurityStrategy)
    @ConditionalOnMissingBean(name = SecurityConstants.ipSecurityStrategy)
    public IpSecurityStrategy ipSecurityStrategy(StrategyHandler strategyHandler, AntiSecurityProperties antiSecurityProperties) {
        return new IpSecurityStrategy(strategyHandler, antiSecurityProperties);
    }

    @Bean(name = SecurityConstants.phoneSecurityStrategy)
    @ConditionalOnMissingBean(name = SecurityConstants.phoneSecurityStrategy)
    public PhoneSecurityStrategy phoneSecurityStrategy(StrategyHandler strategyHandler, AntiSecurityProperties antiSecurityProperties) {
        return new PhoneSecurityStrategy(strategyHandler, antiSecurityProperties);
    }

    @Bean(name = SecurityConstants.ipAndUaSecurityStrategy)
    @ConditionalOnMissingBean(name = SecurityConstants.ipAndUaSecurityStrategy)
    public IpAndUaSecurityStrategy ipAndUaSecurityStrategy(StrategyHandler strategyHandler, AntiSecurityProperties antiSecurityProperties) {
        return new IpAndUaSecurityStrategy(strategyHandler, antiSecurityProperties);
    }

    // service
    @Bean(name = SecurityConstants.securityStrategyFactory)
    @ConditionalOnMissingBean(name = SecurityConstants.securityStrategyFactory)
    public SecurityStrategyFactory securityStrategyFactory(IpSecurityStrategy ipSecurityStrategy, PhoneSecurityStrategy phoneSecurityStrategy, IpAndUaSecurityStrategy ipAndUaSecurityStrategy) {
        return new SecurityStrategyFactory(ipSecurityStrategy, phoneSecurityStrategy, ipAndUaSecurityStrategy);
    }

    @Bean(name = SecurityConstants.antiSecurityService)
    @ConditionalOnMissingBean(name = SecurityConstants.antiSecurityService)
    public AntiSecurityService antiSecurityService(SecurityStrategyFactory securityStrategyFactory) {
        return new AntiSecurityService(securityStrategyFactory);
    }
}
