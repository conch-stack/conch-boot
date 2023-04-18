package ltd.beihu.core.tools.security;

import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;

public abstract class AbstractEsSecret {
    public byte[] encrypt(byte[] data, byte[] key, String algorithm, String mode, String padding) throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        return execute(data, key, algorithm, mode, padding, Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] data, byte[] key, String algorithm, String mode, String padding) throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        return execute(data, key, algorithm, mode, padding, Cipher.DECRYPT_MODE);
    }

    /**
     * hash加密模板
     *
     * @param data 数据
     * @return 密文字节数组
     */
    protected byte[] execute(byte[] data, byte[] key, String algorithm, String mode, String padding, int cipherMode) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
        SecretKeySpec keySpec = new SecretKeySpec(fixKey(algorithm, key), getAlgorithm(algorithm));
        String transformation = MessageFormat.format("{0}/{1}/{2}", getAlgorithm(algorithm), mode, padding);
        Cipher cipher = Cipher.getInstance(transformation);
        SecureRandom random = new SecureRandom();
        cipher.init(cipherMode, keySpec, random);
        return cipher.doFinal(data);
    }

    protected String getAlgorithm(String algorithm) {
        return algorithm;
    }

    protected byte[] fixKey(String algorithm, byte[] key) {
        int keyLength = getKeyLength(algorithm);

        if (keyLength == -1) {//无法修复
            return key;
        }

        if (key.length == keyLength) {//无需修复
            return key;
        }

        if (key.length > keyLength) {
            return ArrayUtils.subarray(key, 0, keyLength);
        } else {
            byte[] fixedKeys = new byte[keyLength];
            System.arraycopy(key, 0, fixedKeys, 0, key.length);
            return fixedKeys;
        }
    }

    //des 8
    //aes 16
    protected int getKeyLength(String algorithm) {
        return -1;
    }
}
