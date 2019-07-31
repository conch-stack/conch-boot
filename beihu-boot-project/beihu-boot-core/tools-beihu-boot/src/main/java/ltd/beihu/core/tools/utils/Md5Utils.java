package ltd.beihu.core.tools.utils;

import ltd.beihu.core.tools.security.HexUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

    /**
     * 计算字符串的MD5值
     *
     * @param string 明文
     * @return 字符串的MD5值
     */
    public static String md5(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes(StandardCharsets.UTF_8));
            return HexUtils.bytes2Hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(string.hashCode());
        }
    }

    /**
     * 计算字符串的MD5值
     *
     * @return 字符串的MD5值
     */
    public static String md5(byte[] sBytes) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(sBytes);
            return HexUtils.bytes2Hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(sBytes.hashCode());
        }
    }
}
