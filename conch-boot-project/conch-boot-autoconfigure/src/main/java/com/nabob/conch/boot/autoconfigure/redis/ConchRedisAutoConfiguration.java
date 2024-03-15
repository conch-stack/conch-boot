package com.nabob.conch.boot.autoconfigure.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.nabob.conch.redis.RedisGenericUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@ConditionalOnClass(RedisGenericUtil.class)
@EnableConfigurationProperties(ConchRedisProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class ConchRedisAutoConfiguration {

    private final ConchRedisProperties conchRedisProperties;

    public ConchRedisAutoConfiguration(ConchRedisProperties conchRedisProperties) {
        this.conchRedisProperties = conchRedisProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisGenericUtil redisGenericUtil(ObjectProvider<StringRedisTemplate> stringRedisTemplateObjectProvider) {
        return new RedisGenericUtil(stringRedisTemplateObjectProvider.getIfAvailable(),
                conchRedisProperties.getPrefix(),
                new GenericJackson2JsonRedisSerializer().configure(objectMapper -> {
                    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                }));
    }

}
