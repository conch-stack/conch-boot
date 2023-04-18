package ltc.beihu.core.redis.boot;

import com.alibaba.fastjson.TypeReference;
import ltd.beihu.core.tools.utils.CollectionExtendUtil;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisUtil {

    protected StringRedisTemplate redisTemplate;

    private String prefix;

    protected RedisSerializers redisSerializers;

    public AbstractRedisUtil(StringRedisTemplate redisTemplate,
                             String prefix,
                             RedisSerializers redisSerializers) {
        this.redisTemplate = redisTemplate;
        this.prefix = prefix;
        this.redisSerializers = redisSerializers;
    }

    public void remove(final String... keys) {
        Arrays.stream(keys).forEach(this::remove);
    }

    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(formatKey(key));
        }
    }

    public boolean exists(final String key) {
        return org.apache.commons.lang3.ObjectUtils.defaultIfNull(redisTemplate.hasKey(formatKey(key)), false);
    }

    public <T> Optional<T> get(final String key, Class<T> clazz) {
        return Optional.ofNullable(parseObject(redisTemplate.opsForValue().get(formatKey(key)), clazz));
    }

    public void set(final String key, Object value) {
        String json = toJSONString(value);
        redisTemplate.opsForValue().set(formatKey(key), json);
    }

    public Boolean setIfAbsent(final String key, Object value) {
        String json = toJSONString(value);
        return redisTemplate.opsForValue().setIfAbsent(formatKey(key), json);
    }

    public void set(final String key, Object value, Long expireTime) {
        String json = toJSONString(value);
        redisTemplate.opsForValue().set(formatKey(key), json, expireTime, TimeUnit.MILLISECONDS);
    }

    public boolean setIfAbsent(final String key, Object value, Long expireTime) {
        String json = toJSONString(value);

        Boolean result = redisTemplate.execute(connection ->
                        connection.set(rawStr(formatKey(key)),
                                rawStr(json),
                                Expiration.from(expireTime, TimeUnit.MILLISECONDS),
                                RedisStringCommands.SetOption.SET_IF_ABSENT),
                true);
        return result == null ? false : result;

//        if (redisTemplate.opsForValue().setIfAbsent(formatKey(key), json)) {
//            expire(key, expireTime, TimeUnit.MILLISECONDS);
//            return true;
//        } else {
//            return false;
//        }
    }

    public Long incr(final String key, long delta) {
        return redisTemplate.opsForValue().increment(formatKey(key), delta);
    }

    public Double incr(final String key, double delta) {
        return redisTemplate.opsForValue().increment(formatKey(key), delta);
    }

    public Boolean expire(final String key, Long timeout) {
        return expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public Boolean expire(final String key, Long timeout, TimeUnit unit) {
        return redisTemplate.expire(formatKey(key), timeout, unit);
    }

    public void expireAt(final String key, Date deadLine) {
        redisTemplate.expireAt(formatKey(key), deadLine);
    }

    public void expireAt(final String key, Long deadLine) {
        redisTemplate.expireAt(formatKey(key), new Date(deadLine));
    }

    public void persist(final String key) {
        redisTemplate.persist(formatKey(key));
    }

    public Long ttl(final String key) {
        return redisTemplate.getExpire(formatKey(key));
    }

    public <T> T leftPop(String key, Class<T> clazz) {
        return parseObject(redisTemplate.opsForList().leftPop(formatKey(key)), clazz);
    }

    public <T> Long leftPush(String key, T obj) {
        String json = toJSONString(obj);
        return redisTemplate.opsForList().leftPush(formatKey(key), json);
    }

    public <T> Long leftPush(String key, List<String> obj) {
        return redisTemplate.opsForList().leftPushAll(formatKey(key), obj);
    }

    public <T> T rightPop(String key, Class<T> clazz) {
        return parseObject(redisTemplate.opsForList().rightPop(formatKey(key)), clazz);
    }


    /**
     * Get the size of list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="http://redis.io/commands/llen">Redis Documentation: LLEN</a>
     */
    @Nullable
    public Long size(String key) {
        return redisTemplate.opsForList().size(formatKey(key));
    }

    public <T> Long rightPush(String key, T obj) {
        String json = toJSONString(obj);
        return redisTemplate.opsForList().leftPush(formatKey(key), json);
    }

    public <T> void setHMap(String key, String hashKey, T obj) {
        String json = toJSONString(obj);
        redisTemplate.opsForHash().put(formatKey(key), hashKey, json);
    }

    public <T> Boolean setHMapIfAbsent(String key, String hashKey, T obj) {
        String json = toJSONString(obj);
        return redisTemplate.opsForHash().putIfAbsent(formatKey(key), hashKey, json);
    }

    public <T> Optional<T> getHMap(String key, String hashKey, Class<T> clazz) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return Optional.ofNullable(parseObject(ops.get(formatKey(key), hashKey), clazz));
    }

    public <T> T getHMap(String key, String hashKey, TypeReference<T> type) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        return parseObject(ops.get(formatKey(key), hashKey), type);
    }

    public void deleteHMap(String key, String hashKey) {
        redisTemplate.opsForHash().delete(formatKey(key), hashKey);
    }

    public <T> Map<String, T> entries(String key, TypeReference<T> type) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, T> rs = new HashMap<>();
        ops.entries(formatKey(key)).forEach((k, v) -> {
            rs.put(k, parseObject(v, type));
        });
        return rs;
    }

    public <T> Map<String, T> entries(String key, Class<T> clazz) {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, T> rs = new HashMap<>();
        ops.entries(formatKey(key)).forEach((k, v) -> {
            rs.put(k, parseObject(v, clazz));
        });
        return rs;
    }

    public Long publish(String channel, Object message) {
        return redisTemplate.execute(connection -> connection.publish(rawStr(formatKey(channel)), rawStr(toJSONString(message))), true);
    }

    public <T> void subscribe(MessageListener<T> messageListener, Class<T> clazz, String... channels) {
        redisTemplate.execute(connection -> {
            connection.subscribe((message, pattern) -> {
                messageListener.onMessage(parseObject(new String(message.getBody()), clazz)
                        , new String(message.getChannel())
                        , pattern == null ? null : new String(pattern));
            }, CollectionExtendUtil.map(channels, channel -> rawStr(formatKey(channel)), byte[][]::new));
            return null;
        }, true);
    }

    public <T> void pSubscribe(MessageListener<T> messageListener, Class<T> clazz, String... patterns) {
        redisTemplate.execute(connection -> {
            connection.pSubscribe((message, pattern) -> {
                messageListener.onMessage(parseObject(new String(message.getBody()), clazz)
                        , new String(message.getChannel())
                        , pattern == null ? null : new String(pattern));
            }, CollectionExtendUtil.map(patterns, pattern -> rawStr(formatKey(pattern)), byte[][]::new));
            return null;
        }, true);
    }

    protected String formatKey(String key) {
        return this.prefix + key;
    }

    protected byte[] rawStr(String key) {
        Assert.notNull(key, "non null key required");
        return redisSerializers.getStringSerializer().serialize(key);
    }

    //region serializer functions
    protected abstract <T> T parseObject(String text, Class<T> clazz);

    protected abstract <T> T parseObject(String text, TypeReference<T> clazz);

    protected abstract String toJSONString(Object object);
    //endregion
}
