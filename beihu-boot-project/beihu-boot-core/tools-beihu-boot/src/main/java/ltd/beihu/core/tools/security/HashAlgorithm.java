package ltd.beihu.core.tools.security;

public enum HashAlgorithm {

    MD5("MD5"), SHA("SHA"), SHA1("SHA1"), SHA256("SHA-256"), SHA512("SHA-512");

    HashAlgorithm(String val) {
        this.val = val;
    }

    String val;

    public String getVal() {
        return val;
    }
}
