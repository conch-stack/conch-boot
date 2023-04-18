package com.nabob.conch.retrofit.retrofit;

import okhttp3.OkHttpClient.Builder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface OkHttpClientFactory {

    Builder createBuilder(boolean disableSslValidation);

    public static class TrustAllHostnames implements HostnameVerifier {
        public TrustAllHostnames() {
        }

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    public static class DisableValidationTrustManager implements X509TrustManager {
        public DisableValidationTrustManager() {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}