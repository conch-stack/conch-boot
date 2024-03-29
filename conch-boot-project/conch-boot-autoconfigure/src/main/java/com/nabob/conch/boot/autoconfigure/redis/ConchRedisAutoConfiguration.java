package com.nabob.conch.boot.autoconfigure.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nabob.conch.redis.RedisGenericUtil;
import com.nabob.conch.redis.RedisSerializers;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.HashMap;
import java.util.Map;

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
    public RedisSerializers redisSerializers() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new CustomRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return new RedisSerializers(jackson2JsonRedisSerializer);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisGenericUtil redisGenericUtil(StringRedisTemplate stringRedisTemplate, RedisSerializers redisSerializers) {
        return new RedisGenericUtil(stringRedisTemplate, conchRedisProperties.getPrefix(), redisSerializers);
    }

    class CustomRedisSerializer<T> extends Jackson2JsonRedisSerializer<T> {

        public CustomRedisSerializer(Class<T> type) {
            super(type);
        }

        public CustomRedisSerializer(JavaType javaType) {
            super(javaType);
        }

        @Override
        @SuppressWarnings("unchecked")
        public byte[] serialize(Object t) throws SerializationException {
            if ((t instanceof Map) && !(t instanceof HashMap)) {
                t = new HashMap((Map) t);
            }
            return super.serialize(t);
        }
    }
}
