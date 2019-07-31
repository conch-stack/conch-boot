package ltd.beihu.core.tools.security;

@EncryptSupport({"des", "desede"})
@DecryptSupport({"des", "desede"})
public class DesSecret extends AbstractEsSecret {

    //des 8
    //3des 24
    protected int getKeyLength(String algorithm) {
        if (EsAlgorithm.DES.getVal().equalsIgnoreCase(algorithm)) {
            return 8;
        }
        if (EsAlgorithm.TRIPLE_DES.getVal().equalsIgnoreCase(algorithm)) {
            return 24;
        }
        return -1;
    }
}
