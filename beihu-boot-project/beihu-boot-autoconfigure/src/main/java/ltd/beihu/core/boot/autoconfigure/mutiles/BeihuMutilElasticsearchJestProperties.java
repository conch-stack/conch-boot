package ltd.beihu.core.boot.autoconfigure.mutiles;

import com.google.common.collect.Maps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/11/14
 * @Version: V1.0.0
 */
@ConfigurationProperties(prefix = "beihu.data.elasticsearch.mutil.jest")
public class BeihuMutilElasticsearchJestProperties {

    public Boolean enable = false;

    /**
     * config elastic instances list
     */
    public Map<String, Instances> instances = Maps.newHashMap();

    public static class Instances{

        /**
         * Comma-separated list of the Elasticsearch instances to use.
         */
        private List<String> uris = new ArrayList<>(
                Collections.singletonList("http://localhost:9200"));

        /**
         * Login username.
         */
        private String username;

        /**
         * Login password.
         */
        private String password;

        /**
         * Whether to enable connection requests from multiple execution threads.
         */
        private boolean multiThreaded = true;

        /**
         * Connection timeout.
         */
        private Duration connectionTimeout = Duration.ofSeconds(3);

        /**
         * Read timeout.
         */
        private Duration readTimeout = Duration.ofSeconds(3);

        public List<String> getUris() {
            return this.uris;
        }

        public void setUris(List<String> uris) {
            this.uris = uris;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isMultiThreaded() {
            return this.multiThreaded;
        }

        public void setMultiThreaded(boolean multiThreaded) {
            this.multiThreaded = multiThreaded;
        }

        public Duration getConnectionTimeout() {
            return this.connectionTimeout;
        }

        public void setConnectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public Duration getReadTimeout() {
            return this.readTimeout;
        }

        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Map<String, Instances> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, Instances> instances) {
        this.instances = instances;
    }

}
