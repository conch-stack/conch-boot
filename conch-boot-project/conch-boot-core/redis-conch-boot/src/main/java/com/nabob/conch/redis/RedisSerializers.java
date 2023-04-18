package com.nabob.conch.redis;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisSerializers {

    private Jackson2JsonRedisSerializer jacksonSerializer;

    public RedisSerializers(Jackson2JsonRedisSerializer jacksonSerializer) {
        this.jacksonSerializer = jacksonSerializer;
    }

    private static RedisSerializer<String> stringSerializer = new StringRedisSerializer();

    public Jackson2JsonRedisSerializer getJacksonSerializer() {
        return jacksonSerializer;
    }

    public RedisSerializer<String> getStringSerializer() {
        return stringSerializer;
    }

    public byte[] serializeString(String key) {
        return stringSerializer.serialize(key);
    }

    public byte[] serialize(Object bean) {
        return jacksonSerializer.serialize(bean);
    }
}
