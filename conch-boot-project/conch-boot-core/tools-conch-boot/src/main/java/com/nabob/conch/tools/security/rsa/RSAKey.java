package com.nabob.conch.tools.security.rsa;

/**
 * Created by tangming on 6/30/17.
 */
public class RSAKey {

    /**
     * 模
     */
    private String modulus;
    /**
     * 公钥指数
     */
    private String publicKey;
    /**
     * 私钥指数
     */
    private String privateKey;

    public String getModulus() {
        return modulus;
    }

    public void setModulus(String modulus) {
        this.modulus = modulus;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
