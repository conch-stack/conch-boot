package ltd.beihu.core.boot.autoconfigure.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ltc.beihu.core.redis.boot.RedisGenericUtil;
import ltc.beihu.core.redis.boot.RedisSerializers;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
@EnableConfigurationProperties(BeihuRedisProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class BeihuRedisAutoConfiguration {

    private final BeihuRedisProperties beihuRedisProperties;

    public BeihuRedisAutoConfiguration(BeihuRedisProperties beihuRedisProperties) {
        this.beihuRedisProperties = beihuRedisProperties;
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
        return new RedisGenericUtil(stringRedisTemplate, beihuRedisProperties.getPrefix(), redisSerializers);
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
