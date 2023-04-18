package ltd.beihu.core.tools.security;

@EncryptSupport({"aes", "aes128", "aes256"})
@DecryptSupport({"aes", "aes128", "aes256"})
public class AesSecret extends AbstractEsSecret {

    //aes 16
    protected int getKeyLength(String algorithm) {

        if (EsAlgorithm.AES.getVal().equalsIgnoreCase(algorithm)) {
            return 16;
        }
        if (EsAlgorithm.AES128.getVal().equalsIgnoreCase(algorithm)) {
            return 16;
        }
        if (EsAlgorithm.AES256.getVal().equalsIgnoreCase(algorithm)) {
            return 32;
        }
        return -1;
    }

    @Override
    protected String getAlgorithm(String algorithm) {
        return "aes";
    }
}
