package ltd.beihu.core.tools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tangming on 4/27/17.
 */
public class IDUtils {

    /**
     * uuid:no bar
     */
    public static String UUID() {
        return UUID2Tidy(UUID.randomUUID()).toLowerCase();
    }

    private static String UUID2Tidy(UUID uuid) {
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        return (DigitUtils.digits(mostSigBits >> 32, 8) +
                DigitUtils.digits(mostSigBits >> 16, 4) +
                DigitUtils.digits(mostSigBits, 4) +
                DigitUtils.digits(leastSigBits >> 48, 4) +
                DigitUtils.digits(leastSigBits, 12));
    }

    /**
     * uuid压缩至16位短id
     */
    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long meanSign = DigitUtils.reversalMean(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        return (DigitUtils.digits(meanSign >> 32, 8) +
                DigitUtils.digits(meanSign, 8));
    }

    private final static String lowSChars = "0123456789abcdefghijklmnopqrstuvwxyz";

    private final static String digitChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成一个简单的id
     * 包含数字及大小写字符
     *
     * @param length -id长度
     * @return -liteId
     * @see #liteId(int, boolean)
     */
    public static String liteId(int length) {
        return liteId(length, true);
    }

    /**
     * 生成一个简单的id
     *
     * @param length      -id长度
     * @param supportCase -区分大小写
     * @return -liteId
     * @see #liteId(int)
     */
    public static String liteId(int length, boolean supportCase) {
        Assert.isTrue(length > 0, "liteId cannot be empty!");
        String chars = supportCase ? digitChars : lowSChars;

        int unit = 128 / (32 - Integer.numberOfLeadingZeros(chars.length() - 1));

        if (length > unit) {
            int group = (length - 1) / unit;
            int groupLength = length / (group + 1);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < group; i++) {
                sb.append(buildLiteId(groupLength, chars));
            }
            sb.append(buildLiteId(length - group * groupLength, chars));
            return sb.toString();
        } else {
            return buildLiteId(length, chars);
        }
    }

    private static String buildLiteId(int length, String chars) {
        UUID uuid = UUID.randomUUID();
        long high = uuid.getMostSignificantBits();
        long low = uuid.getLeastSignificantBits();

        int offset = 128 / length;
        int dcLength = chars.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            long v = DigitUtils.sub(high, low, offset * i, offset);
            sb.append(chars.charAt(DigitUtils.mod(v, dcLength)));
        }
        return sb.toString();
    }

    public static String timeId() {
        //第一部分是当前时间串-17位
        SimpleDateFormat SDF = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        String timed = SDF.format(new Date());
        long tail = Long.parseLong(timed.substring(16));
        UUID uuid = UUID.randomUUID();
        long meanSign = (tail << 60) + DigitUtils.reversalMean(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        return timed.substring(0, 16) + (DigitUtils.digits(meanSign >>> 32, 8) +
                DigitUtils.digits(meanSign, 8));
    }

}
