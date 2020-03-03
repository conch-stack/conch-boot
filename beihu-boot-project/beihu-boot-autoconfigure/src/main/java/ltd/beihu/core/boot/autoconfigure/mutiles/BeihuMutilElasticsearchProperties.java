package ltd.beihu.core.boot.autoconfigure.mutiles;

import com.google.common.collect.Maps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/11/14
 * @Version: V1.0.0
 */
@ConfigurationProperties(prefix = "beihu.data.elasticsearch.mutil.transport")
public class BeihuMutilElasticsearchProperties {

    public Boolean enable = false;
    /**
     * config elastic instances list
     */
    public Map<String, Instances> instances = Maps.newHashMap();

    public static class Instances{

        private String clusterName = "elasticsearch";
        private String clusterNodes;
        private Map<String, String> properties = Maps.newHashMap();

        public String getClusterName() {
            return this.clusterName;
        }

        public void setClusterName(String clusterName) {
            this.clusterName = clusterName;
        }

        public String getClusterNodes() {
            return this.clusterNodes;
        }

        public void setClusterNodes(String clusterNodes) {
            this.clusterNodes = clusterNodes;
        }

        public Map<String, String> getProperties() {
            return this.properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
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
