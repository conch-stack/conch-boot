package ltd.beihu.core.tools.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@EncryptSupport({"HMACMD5", "HMACSHA1", "HMACSHA256", "HMACSHA512"})
public class HmacEncryptor {

    /**
     * hash加密模板
     *
     * @param data      数据
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    public static byte[] encrypt(byte[] data, byte[] key, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
        SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKey);
        return mac.doFinal(data);
    }
}
