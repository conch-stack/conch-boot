package ltd.beihu.core.boot.autoconfigure.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.redis.beihu")
public class BeihuRedisProperties {

    private String prefix = "p_";

    private String lock = "l_";

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
