package com.nabob.conch.retrofit.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.Collections;
import java.util.List;

/**
 * Retrofit Client Builder
 * <p>
 * The CallAdapter of Retrofit2 has default value
 * Support {@link retrofit2.Call}
 * Support {@link java.util.concurrent.CompletableFuture}
 *
 * @author Adam
 * @date 2020/8/1
 */
public class RetrofitClientBuildDelegate {

    private OkHttpClient okHttpClient;
    private ObjectMapper mapper;
    private Gson gson;
    private String converterName;

    public RetrofitClientBuildDelegate(OkHttpClient okHttpClient, ObjectMapper mapper, Gson gson, String converterName) {
        this.okHttpClient = okHttpClient;
        this.mapper = mapper;
        this.gson = gson;
        this.converterName = (null == converterName || converterName.isEmpty()) ? "jackson" : converterName;
    }

    /**
     * Get client use default CallAdapter.Factory
     *
     * @param baseUrl base url
     * @param clazz   target class
     * @param <T>     the generic type of the target class
     * @return the proxy bean of target class
     */
    public <T> T getClient(String baseUrl, Class<T> clazz) {
        Retrofit retrofit = getRetrofit(baseUrl, this.okHttpClient, null, Collections.singletonList(determineConverterFactory()));
        return retrofit.create(clazz);
    }

    /**
     * Determine Converter Factory
     *
     * @return {@link Converter.Factory}
     */
    private Converter.Factory determineConverterFactory() {
        switch (this.converterName) {
            case "gson":
                return (null != this.gson) ? GsonConverterFactory.create(this.gson) : GsonConverterFactory.create();
            case "jackson":
            default:
                return (null != this.mapper) ? JacksonConverterFactory.create(this.mapper) : JacksonConverterFactory.create();
        }
    }

    /**
     * get a retrofit instance by config
     *
     * @param baseUrl              base url
     * @param client               okhttp client instance
     * @param callAdapterFactories call adapters : has default implements
     * @param converterFactories   converters
     * @return {@link Retrofit}
     */
    private static Retrofit getRetrofit(String baseUrl, OkHttpClient client, List<CallAdapter.Factory> callAdapterFactories,
                                        List<Converter.Factory> converterFactories) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client);
        if (null != callAdapterFactories && !callAdapterFactories.isEmpty()) {
            callAdapterFactories.forEach(retrofitBuilder::addCallAdapterFactory);
        }
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
        if (null != converterFactories && !converterFactories.isEmpty()) {
            converterFactories.forEach(retrofitBuilder::addConverterFactory);
        }

        return retrofitBuilder.build();
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setConverterName(String converterName) {
        this.converterName = converterName;
    }
}