package ltd.beihu.core.web.boot.http;

import ltd.beihu.core.tools.code.BasicServiceCode;
import ltd.beihu.core.web.boot.exception.ServiceException;
import ltd.beihu.core.web.boot.response.BasicResponse;
import ltd.beihu.core.web.boot.response.JsonResponse;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RetrofitUtil {

    private static final Logger logger = LoggerFactory.getLogger(RetrofitUtil.class);

    private static final ThreadPoolExecutor HTTP_EXECUTOR = new ThreadPoolExecutor(50, Integer.MAX_VALUE,
            180, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            Util.threadFactory("OkHttp Dispatcher", false));

    public static <T> T getClient(String baseURL, Class<T> clazz) {
        return getClient(baseURL, clazz, FastJsonConverterFactory.create());
    }

    public static <T> T getClient(String baseURL, Class<T> clazz, boolean followRedirects) {
        return getClient(baseURL, clazz, FastJsonConverterFactory.create(), getOkHttpBuilder(followRedirects).build());
    }


    private static <T> T getClient(String baseURL, Class<T> clazz, Converter.Factory factory) {
        return getClient(baseURL, clazz, factory, getOkHttpBuilder(true).build());
    }

    private static <T> T getClient(String baseURL, Class<T> clazz, Converter.Factory factory, OkHttpClient client) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(factory)
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(clazz);
    }

    public static <T> T getHttpsClient(String baseUrl, Class<T> clazz, File file, String password) {
        return getClient(baseUrl, clazz, FastJsonConverterFactory.create(), getOkHttps(file, password.toCharArray()));
    }

    public static <T> T getHttpsClient(String baseUrl, Class<T> clazz) {
        return getClient(baseUrl, clazz, FastJsonConverterFactory.create(), getOkHttpsClient());
    }

    private static OkHttpClient.Builder getOkHttpBuilder(boolean redirect) {
        Dispatcher dispatcher = new Dispatcher(HTTP_EXECUTOR);
        return new OkHttpClient.Builder().dispatcher(dispatcher)
//                .hostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .followRedirects(redirect)
                .followSslRedirects(redirect)
                .readTimeout(HTTP_EXECUTOR.getKeepAliveTime(TimeUnit.SECONDS), TimeUnit.SECONDS)
                .writeTimeout(HTTP_EXECUTOR.getKeepAliveTime(TimeUnit.SECONDS), TimeUnit.SECONDS)
                .connectTimeout(HTTP_EXECUTOR.getKeepAliveTime(TimeUnit.SECONDS), TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HttpLogInterceptor());
    }

    static OkHttpClient getOkHttps(File file, char[] password) {

        try (FileInputStream inputStream = new FileInputStream(file)) {
            // 1.设置KeyStore: 证书格式为PKCS12, 密码为商户ID
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(inputStream, password);
            return getOkHttpsClient(password, keyStore);

        } catch (Exception e) {

        }
        return null;
    }

    private static OkHttpClient getOkHttpsClient(char[] password, KeyStore keyStore) {
        try {
            // 2.设置TrustManager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            // 3.设置KeyManager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

            // 4.设置SSLContext: 指定TLS版本, 只允许TLSv1安全套接字协议
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return getOkHttpBuilder(true).sslSocketFactory(sslSocketFactory, trustManager).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static OkHttpClient getOkHttpsClient() {
        try {
            //创建一个不验证证书链的证书信任管理器。
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            // 4.设置SSLContext: 指定TLS版本, 只允许TLSv1安全套接字协议
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return getOkHttpBuilder(true).sslSocketFactory(sslSocketFactory, trustManager).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;

    }


    public static <T> T readResult(Response<BasicResponse<T>> response) {
        if (response != null) {
            if (response.isSuccessful()) {
                BasicResponse<T> basicResponse = response.body();
                if (basicResponse != null) {
                    if (basicResponse.getCode() == 0) {
                        return basicResponse.getData();
                    } else {
                        logger.error("error in readResult:{}, code :{}", basicResponse.getMessage(), basicResponse.getCode());
                    }
                }
            } else {
                logger.error("error in readResult:" + response.message());
            }
        }
        throw new ServiceException(BasicServiceCode.SERVER_ERROR);
    }

    public static void checkResponse(Response response) {
        if (response != null) {
            if (response.isSuccessful()) {
                Object body = response.body();
                if (body != null) {
                    if (body instanceof JsonResponse) {
                        JsonResponse apiCoreResult = (JsonResponse) body;
                        if (apiCoreResult.getCode() == 0) {
                            return;
                        }
                    }
                }
            } else {
                logger.error("error in checkResponse:" + response.message());
            }
        }
        throw new ServiceException(BasicServiceCode.SERVER_ERROR);
    }

    public static <T> T readBody(Response<T> response) {
        if (response != null) {
            if (response.isSuccessful()) {
                T body = response.body();
                if (body != null) {
                    return body;
                }
            } else {
                logger.error("error to call " + response.headers().toString() + ", message:" + response.message());
                logger.error("*****************error body start******************");
                try {
                    logger.error("【{}】", response.errorBody() == null ? "" : response.errorBody().string());
                } catch (IOException e) {
                }
                logger.error("*****************error body end******************");
            }
        }
        throw new ServiceException(BasicServiceCode.SERVER_ERROR);
    }

}