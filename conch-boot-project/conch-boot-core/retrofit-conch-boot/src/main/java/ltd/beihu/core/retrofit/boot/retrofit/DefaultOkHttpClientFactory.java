package ltd.beihu.core.retrofit.boot.retrofit;

import okhttp3.OkHttpClient.Builder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DefaultOkHttpClientFactory implements OkHttpClientFactory {

    private static final Log LOG = LogFactory.getLog(DefaultOkHttpClientFactory.class);
    private Builder builder;

    public DefaultOkHttpClientFactory(Builder builder) {
        this.builder = builder;
    }

    @Override
    public Builder createBuilder(boolean disableSslValidation) {
        if (disableSslValidation) {
            try {
                X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
                TrustManager[] trustManagers = new TrustManager[]{disabledTrustManager};
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init((KeyManager[])null, trustManagers, new SecureRandom());
                SSLSocketFactory disabledSSLSocketFactory = sslContext.getSocketFactory();
                this.builder.sslSocketFactory(disabledSSLSocketFactory, disabledTrustManager);
                this.builder.hostnameVerifier(new TrustAllHostnames());
            } catch (NoSuchAlgorithmException var6) {
                LOG.warn("Error setting SSLSocketFactory in OKHttpClient", var6);
            } catch (KeyManagementException var7) {
                LOG.warn("Error setting SSLSocketFactory in OKHttpClient", var7);
            }
        }

        return this.builder;
    }
}
