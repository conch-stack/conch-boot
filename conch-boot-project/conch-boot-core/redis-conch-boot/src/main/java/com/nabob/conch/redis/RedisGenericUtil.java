package com.nabob.conch.redis;

import com.alibaba.fastjson.TypeReference;
import com.nabob.conch.tools.utils.CollectionExtendUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.lang.Nullable;
import redis.clients.jedis.util.SafeEncoder;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"all"})
public class RedisGenericUtil extends AbstractRedisUtil {

    public RedisGenericUtil(StringRedisTemplate redisTemplate, String prefix, GenericJackson2JsonRedisSerializer redisSerializer) {
        super(redisTemplate, prefix, redisSerializer);
    }

    public StringRedisTemplate getRedisTemplate() {
        return super.redisTemplate;
    }

    public <T> Optional<T> get(final String key) {
        return Optional.ofNullable(parseObject(redisTemplate.opsForValue().get(formatKey(key))));
    }

    public <T> T leftPop(String key) {
        return parseObject(redisTemplate.opsForList().leftPop(formatKey(key)));
    }

    public Long leftPush(String key, String value) {
        Long leftPush = redisTemplate.opsForList().leftPush(formatKey(key), value);
        return leftPush;
    }

    public <T> T rightPopAndLeftPush(String sourceKey, String destinationKey) {
        return parseObject(redisTemplate.opsForList().rightPopAndLeftPush(formatKey(sourceKey), formatKey(destinationKey)));
    }

    public <T> T rightPop(String key) {
        return parseObject(redisTemplate.opsForList().rightPop(formatKey(key)));
    }

    public <T> Optional<T> getHMap(String key, String hashKey) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return Optional.ofNullable(parseObject(ops.get(formatKey(key), hashKey)));
    }

    public <T> void subscribe(MessageListener<T> messageListener, String... channels) {
        redisTemplate.execute(connection -> {
            connection.subscribe((message, pattern) -> {
                messageListener.onMessage(parseObject(new String(message.getBody()))
                        , new String(message.getChannel())
                        , pattern == null ? null : new String(pattern));
            }, CollectionExtendUtil.map(channels, channel -> rawStr(formatKey(channel)), byte[][]::new));
            return null;
        }, true);
    }

    public <T> void pSubscribe(MessageListener<T> messageListener, String... patterns) {
        redisTemplate.execute(connection -> {
            connection.pSubscribe((message, pattern) -> {
                messageListener.onMessage(parseObject(new String(message.getBody()))
                        , new String(message.getChannel())
                        , pattern == null ? null : new String(pattern));
            }, CollectionExtendUtil.map(patterns, pattern -> rawStr(formatKey(pattern)), byte[][]::new));
            return null;
        }, true);
    }

    //region serializer functions

    protected <T> T parseObject(String text) {
        return (T) redisSerializers.deserialize(toBytes(text));
    }

    @Override
    protected <T> T parseObject(String text, Class<T> clazz) {
        return parseObject(text);
    }

    @Override
    protected <T> T parseObject(String text, TypeReference<T> clazz) {
        return parseObject(text);
    }

    @Override
    protected String toJSONString(Object object) {
        return toString(redisSerializers.serialize(object));
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(formatKey(pattern));
    }
    //endregion

    @Nullable
    public static byte[] toBytes(@Nullable String source) {
        return source == null ? null : SafeEncoder.encode(source);
    }

    @Nullable
    public static String toString(@Nullable byte[] source) {
        return source == null ? null : SafeEncoder.encode(source);
    }
}
