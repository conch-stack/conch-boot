package com.nabob.conch.boot.autoconfigure.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: zjz
 * @Desc: configure properties
 *      Curator 2.x.x - compatible with both ZooKeeper 3.4.x and ZooKeeper 3.5.x
 *      Curator 3.x.x - compatible only with ZooKeeper 3.5.x and includes support for new features such as dynamic reconfiguration, etc.
 * @Date: 18-6-7
 * @Version: V1.0.0
 */
@ConfigurationProperties(prefix = "conch.zk")
public class ConchZookeeperProperties {

    private boolean enable = false;

    /**
     * zk server lists
     *      host:port concat by ','
     */
    private String zhosts;

    /**
     * session timeout unit: ms
     */
    private int sessionTimeout;

    /**
     * connection timeout unit: ms
     */
    private int connectionTimeout;

    /**
     * retry policy
     */
    private RetryPolicy retryPolicy;

    /**
     * namespace
     */
    private String namespace;

    /**
     * retry policy
     *      choose ExponentialBackofRetry
     */
    public static class RetryPolicy {

        /**
         * retry max count
         */
        private int retryNum;

        /**
         * init sleep time  unit: ms
         */
        private int baseSleepTime;

        /**
         * max sleep time unit: ms
         */
        private int maxSleepTime;

        public int getRetryNum() {
            return retryNum;
        }

        public void setRetryNum(int retryNum) {
            this.retryNum = retryNum;
        }

        public int getBaseSleepTime() {
            return baseSleepTime;
        }

        public void setBaseSleepTime(int baseSleepTime) {
            this.baseSleepTime = baseSleepTime;
        }

        public int getMaxSleepTime() {
            return maxSleepTime;
        }

        public void setMaxSleepTime(int maxSleepTime) {
            this.maxSleepTime = maxSleepTime;
        }
    }

    /**
     * todo :
     *      acl
     *      compression gzip
     */

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getZhosts() {
        return zhosts;
    }

    public void setZhosts(String zhosts) {
        this.zhosts = zhosts;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
