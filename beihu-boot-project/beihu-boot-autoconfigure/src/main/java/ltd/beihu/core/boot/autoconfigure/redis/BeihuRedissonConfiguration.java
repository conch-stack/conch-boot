package ltd.beihu.core.boot.autoconfigure.redis;

import ltc.beihu.core.redis.boot.redisson.RedissonUtils;
import ltd.beihu.core.tools.utils.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureAfter(BeihuRedisAutoConfiguration.class)
public class BeihuRedissonConfiguration {

    private RedisProperties redisProperties;

    public BeihuRedissonConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient getRedisson() {

        Config config = new Config();

        if (redisProperties.getSentinel() != null) {
            RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
            config.useSentinelServers()
                    .setDatabase(redisProperties.getDatabase())
                    .setMasterName(sentinel.getMaster())
                    .addSentinelAddress(formatNodes(sentinel.getNodes()))
                    .setPassword(redisProperties.getPassword());
        } else if (redisProperties.getCluster() != null) {
            RedisProperties.Cluster cluster = redisProperties.getCluster();
            config.useClusterServers()
                    .addNodeAddress(formatNodes(cluster.getNodes()))
                    .setPassword(redisProperties.getPassword());
        } else if (StringUtils.hasText(redisProperties.getUrl())) {
            config.useSingleServer().setAddress(redisProperties.getUrl())
                    .setPassword(redisProperties.getPassword());
        } else {
            config.useSingleServer()
                    .setAddress(MessageFormat.format("redis://{0}:{1}",
                            redisProperties.getHost(), String.valueOf(redisProperties.getPort())))
                    .setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonUtils redissonUtils(RedissonClient redissonClient, BeihuRedisProperties beihuRedisProperties) {
        return new RedissonUtils(redissonClient, beihuRedisProperties.getPrefix(), beihuRedisProperties.getLock());
    }

    private String[] formatNodes(List<String> nodes) {
        if (nodes == null)
            return new String[0];
        return nodes.stream()
                .filter(Objects::nonNull)
                .map(node -> StringUtil.addPrefix(node, "redis://"))
                .toArray(String[]::new);
    }
}
