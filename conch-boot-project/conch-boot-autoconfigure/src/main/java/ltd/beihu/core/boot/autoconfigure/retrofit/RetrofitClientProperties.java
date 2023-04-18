package ltd.beihu.core.boot.autoconfigure.retrofit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author Adam
 */
@ConfigurationProperties("beihu.retrofit.client")
public class RetrofitClientProperties {

    /**
     * value for converterName
     */
    private String converterName = "jackson";

    /**
     * value for max number od connections.
     */
    private int maxConnections = 200;

    /**
     * value for time to live.
     */
    private long timeToLive = 900L;

    /**
     * time to live unit.
     */
    private TimeUnit timeToLiveUnit = TimeUnit.SECONDS;

    /**
     * value for connection timeout.
     */
    private int connectionTimeout = 2000;

    /**
     * value for read timeout.
     */
    private int readTimeout = 2000;

    /**
     * value for write timeout.
     */
    private int writeTimeout = 2000;

    /**
     * value for following redirects.
     */
    private boolean followRedirects = true;

    /**
     * value for disabling SSL validation.
     */
    private boolean disableSslValidation = false;

    /**
     * value for enable log print.
     */
    private boolean enableLog = false;

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public TimeUnit getTimeToLiveUnit() {
        return this.timeToLiveUnit;
    }

    public void setTimeToLiveUnit(TimeUnit timeToLiveUnit) {
        this.timeToLiveUnit = timeToLiveUnit;
    }

    public boolean isFollowRedirects() {
        return this.followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getConverterName() {
        return converterName;
    }

    public void setConverterName(String converterName) {
        this.converterName = converterName;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public boolean isDisableSslValidation() {
        return disableSslValidation;
    }

    public void setDisableSslValidation(boolean disableSslValidation) {
        this.disableSslValidation = disableSslValidation;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public void setEnableLog(boolean enableLog) {
        this.enableLog = enableLog;
    }
}
