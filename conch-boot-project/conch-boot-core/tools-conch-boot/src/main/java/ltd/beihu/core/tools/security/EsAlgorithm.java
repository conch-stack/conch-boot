package ltd.beihu.core.tools.security;

/**
 * Encryption Standard Algorithm
 */
public enum EsAlgorithm {

    DES("des"),

    /**
     * as @{code DESEDE}
     *
     * @see EsAlgorithm#DESEDE
     */
    TRIPLE_DES("desede"),
    /**
     * @see EsAlgorithm#TRIPLE_DES
     */
    DESEDE("desede"),

    /**
     * @see EsAlgorithm#AES128
     */
    AES("aes"),

    /**
     * @see EsAlgorithm#AES
     */
    AES128("aes128"),

    /**
     * <p>
     * For support AES256 you may need to download this file based on your jdk version:
     * <p>
     * <a href="http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html">Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 6 Download</a>
     * <p>
     * <a href="http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html">Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 7 Download</a>
     * <p>
     * <a href="http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html">Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 8 Download</a>
     * <p>
     * <p>
     * Extract the jar files from the zip and save them in ${java.home}/jre/lib/security/.
     */
    AES256("aes256");

    EsAlgorithm(String val) {
        this.val = val;
    }

    String val;

    public String getVal() {
        return val;
    }

    public enum Mode {
        //电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB
        ECB, CBC, CFB, OFB
    }

    public enum PADDING {
        NOPADDING("NoPadding"),
        ZEROSPADDING("ZerosPadding"),
        PKCS5PADDING("PKCS5Padding"),
        ISO10126PADDING("ISO10126Padding"),
        PKCS7PADDING("PKCS7Padding");

        PADDING(String val) {
            this.val = val;
        }

        String val;

        public String getVal() {
            return val;
        }
    }
}
