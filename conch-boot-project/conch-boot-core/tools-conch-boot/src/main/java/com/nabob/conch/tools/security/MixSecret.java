package com.nabob.conch.tools.security;

import com.nabob.conch.tools.utils.BytesUtils;
import org.apache.commons.lang3.ArrayUtils;

@EncryptSupport("mix")
@DecryptSupport("mix")
public class MixSecret {

    public static byte[] encrypt(byte[] bytes) {
        return mix(bytes);
    }

    public static byte[] mix(byte[] bytes) {
        try {
            if (ArrayUtils.isEmpty(bytes)) {
                return bytes;
            }
            int offset = offset((bytes.length));
            int tailLength = bytes.length >> 1;
            int headLength = tailLength + (bytes.length & 1);
            int quarter = tailLength >> 2;

            byte[] headBytes = new byte[headLength];
            byte[] tailBytes = new byte[tailLength];
            int ri = 0;
            for (int i = 0; i < tailLength; i++) {
                headBytes[left_relocation_index(i, headLength, quarter)] = mix(bytes[ri], offset + ri);
                ri = ri | 1;
                tailBytes[right_relocation_index(i, tailLength, quarter)] = mix(bytes[ri], offset + ri);
                ri = ri + 1;
            }
            if ((bytes.length & 1) == 1) {
                headBytes[left_relocation_index(tailLength, headLength, quarter)] = mix(bytes[ri], offset + ri);
            }
            return BytesUtils.concat(headBytes, tailBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("");
        }
    }

    public static byte[] decrypt(byte[] bytes) {
        return unmix(bytes);
    }

    public static byte[] unmix(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            return bytes;
        }
        int offset = offset((bytes.length));

        int tailLength = bytes.length / 2;
        int headLength = tailLength + (bytes.length & 1);
        int quarter = tailLength >> 2;

        byte[] headBytes = subBytes(bytes, 0, headLength);
        byte[] tailBytes = subBytes(bytes, headLength, tailLength);
        byte[] resultBytes = new byte[bytes.length];
        int ri = 0;
        for (int i = 0; i < tailBytes.length; i++) {
            resultBytes[ri] = unMix(headBytes[left_relocation_index(i, headLength, quarter)], offset + ri);
            ri = ri | 1;
            resultBytes[ri] = unMix(tailBytes[right_relocation_index(i, tailLength, quarter)], offset + ri);
            ri = ri + 1;
        }
        if (tailLength != headLength) {
            resultBytes[ri] = unMix(headBytes[left_relocation_index(tailLength, headLength, quarter)], offset + ri);
        }
        return resultBytes;
    }

    private static int maxByte = 0b11111111;
    private static int ultraByte = 0b100000000;

    private static int offset(int length) {
        return (((length << 4) + (length << 3) + (length << 2) + (length << 1) + length) & (maxByte >> 1)) + (maxByte >> 2);
    }

    private static byte mix(byte b, int offset) {
        return (byte) ((((int) b) + offset) & maxByte);
    }

    private static byte unMix(byte b, int offset) {
        return (byte) (((((int) b) | ultraByte) - offset) & maxByte);
    }

    private static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    private static int right_relocation_index(int index, int length, int quarter) {
        index = index + quarter;
        if (index >= length) {
            return index - length;
        } else {
            return index;
        }
    }

    private static int left_relocation_index(int index, int length, int quarter) {
        if (index < quarter) {
            return index + length - quarter;
        } else {
            return index - quarter;
        }
    }

}
