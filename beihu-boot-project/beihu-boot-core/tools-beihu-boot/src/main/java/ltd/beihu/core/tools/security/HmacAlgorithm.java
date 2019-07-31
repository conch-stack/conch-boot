package ltd.beihu.core.tools.security;

public enum HmacAlgorithm {

    HMACMD5("HMACMD5"), HMACSHA1("HMACSHA1"), HMACSHA256("HMACSHA256"), HMACSHA512("HMACSHA512");

    HmacAlgorithm(String val) {
        this.val = val;
    }

    String val;

    public String getVal() {
        return val;
    }
}
