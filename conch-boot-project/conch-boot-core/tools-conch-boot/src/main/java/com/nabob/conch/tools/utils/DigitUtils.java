package com.nabob.conch.tools.utils;

/**
 * Created by tangming on 5/25/17.
 */
public class DigitUtils {
    public static int mod(long x, int m) {
        int result = (int) (x % m);
        return (result >= 0) ? result : result + m;
    }

    public static long sub(long high, long low, int index, int length) {
        if (index < 64) {
            if (index + length <= 64) {
                return sub(high, index, length);
            } else {
                int lowLength = index + length - 64;
                int highLength = length - lowLength;
                return (sub(high, 64 - highLength, highLength) << lowLength)
                        + sub(low, 0, lowLength);
            }
        } else {
            return sub(low, index - 64, length);
        }
    }

    public static long sub(long val, int index, int length) {
        if (length == 0) {
            return 0;
        }
        //remove pre
        return (val << index) >>> (64 - length);
    }

    /**
     * 翻转取均值
     */
    public static long reversalMean(long x, long y) {
        x = (x >> 16) + (x << 48);
        y = (y >> 48) + (y << 16);
        return (x & y) + ((x ^ y) >> 1);
    }

    /**
     * Returns val represented by the specified number of hex digits.
     */
    public static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
