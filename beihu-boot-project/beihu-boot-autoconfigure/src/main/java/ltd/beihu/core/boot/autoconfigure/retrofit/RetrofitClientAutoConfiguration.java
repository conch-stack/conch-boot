package ltd.beihu.core.boot.autoconfigure.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ltd.beihu.core.retrofit.boot.interceptor.HttpLogInterceptor;
import ltd.beihu.core.retrofit.boot.retrofit.DefaultOkHttpClientConnectionPoolFactory;
import ltd.beihu.core.retrofit.boot.retrofit.DefaultOkHttpClientFactory;
import ltd.beihu.core.retrofit.boot.retrofit.OkHttpClientConnectionPoolFactory;
import ltd.beihu.core.retrofit.boot.retrofit.OkHttpClientFactory;
import ltd.beihu.core.retrofit.boot.retrofit.RetrofitClientBuildDelegate;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit Client AutoConfiguration
 *
 * @author Adam
 * @date 2020/8/3
 */
@Configuration
@ConditionalOnClass({OkHttpClient.class, Retrofit.class})
@ConditionalOnMissingBean(OkHttpClient.class)
@EnableConfigurationProperties(RetrofitClientProperties.class)
public class RetrofitClientAutoConfiguration {

    private OkHttpClient okHttpClient;

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClientConnectionPoolFactory connPoolFactory() {
        return new DefaultOkHttpClientConnectionPoolFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClientFactory okHttpClientFactory(OkHttpClient.Builder builder) {
        return new DefaultOkHttpClientFactory(builder);
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionPool.class)
    public ConnectionPool httpClientConnectionPool(
            RetrofitClientProperties retrofitClientProperties,
            OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        Integer maxTotalConnections = retrofitClientProperties.getMaxConnections();
        Long timeToLive = retrofitClientProperties.getTimeToLive();
        TimeUnit ttlUnit = retrofitClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    public OkHttpClient client(OkHttpClientFactory httpClientFactory,
                                       ConnectionPool connectionPool,
                                       RetrofitClientProperties retrofitClientProperties) {
        Boolean followRedirects = retrofitClientProperties.isFollowRedirects();
        Integer connectTimeout = retrofitClientProperties.getConnectionTimeout();
        Integer readTimeout = retrofitClientProperties.getReadTimeout();
        Integer writeTimeout = retrofitClientProperties.getWriteTimeout();
        Boolean disableSslValidation = retrofitClientProperties.isDisableSslValidation();
        Boolean enableLog = retrofitClientProperties.isEnableLog();
        OkHttpClient.Builder builder = httpClientFactory.createBuilder(disableSslValidation)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects).connectionPool(connectionPool);
        if (enableLog) {
            builder.addInterceptor(new HttpLogInterceptor());
        }
        this.okHttpClient = builder.build();
        return this.okHttpClient;
    }

    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }
    }

    @Bean
    @ConditionalOnMissingBean(RetrofitClientBuildDelegate.class)
    public RetrofitClientBuildDelegate retrofitClientBuildDelegate(OkHttpClient client,
                                                                   RetrofitClientProperties retrofitClientProperties,
                                                                   ObjectProvider<ObjectMapper> mapper,
                                                                   ObjectProvider<Gson> gson) {
        return new RetrofitClientBuildDelegate(client, mapper.getIfAvailable(), gson.getIfAvailable(), retrofitClientProperties.getConverterName());
    }

}
