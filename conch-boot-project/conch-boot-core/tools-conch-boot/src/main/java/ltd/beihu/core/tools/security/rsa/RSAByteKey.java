package ltd.beihu.core.tools.security.rsa;

/**
 * Created by tangming on 6/30/17.
 */
public class RSAByteKey {

    /**
     * 公钥指数
     */
    private byte[] publicKey;
    /**
     * 私钥指数
     */
    private byte[] privateKey;

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }
}
