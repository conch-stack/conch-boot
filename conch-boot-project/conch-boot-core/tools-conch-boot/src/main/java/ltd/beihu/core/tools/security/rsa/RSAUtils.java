package ltd.beihu.core.tools.security.rsa;

import ltd.beihu.core.tools.security.DecryptSupport;
import ltd.beihu.core.tools.security.EncryptSupport;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

/**
 * Created by tangming on 6/30/17.
 */
@EncryptSupport("rsa")
@DecryptSupport("rsa")
public class RSAUtils {

    public static RSAKey createRSAKey(byte[] seed) throws Exception {

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(seed.length, new SecureRandom(seed));
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey();

        rsaKey.setModulus(publicKey.getModulus().toString(Character.MAX_RADIX));
        rsaKey.setPublicKey(publicKey.getPublicExponent().toString(Character.MAX_RADIX));
        rsaKey.setPrivateKey(privateKey.getPrivateExponent().toString(Character.MAX_RADIX));

        return rsaKey;
    }

    public static RSAByteKey createRSAByteKey(byte[] seed) throws Exception {

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(seed.length, new SecureRandom(seed));
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAByteKey rsaKey = new RSAByteKey();

        rsaKey.setPublicKey(publicKey.getEncoded());
        rsaKey.setPrivateKey(privateKey.getEncoded());

        return rsaKey;
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger b1 = new BigInteger(modulus, Character.MAX_RADIX);
        BigInteger b2 = new BigInteger(exponent, Character.MAX_RADIX);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger b1 = new BigInteger(modulus, Character.MAX_RADIX);
        BigInteger b2 = new BigInteger(exponent, Character.MAX_RADIX);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * 得到私钥
     *
     * @param key 密钥
     */
    public static RSAPrivateKey getPrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 得到公钥
     *
     * @param key 密钥
     */
    public static RSAPublicKey getPublicKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }


    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     */
    public static byte[] getPublicKeyByte(RSAKey rsaKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return getPublicKey(rsaKey.getModulus(), rsaKey.getPublicKey()).getEncoded();
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     */
    public static byte[] getPrivateKeyByte(RSAKey rsaKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return getPrivateKey(rsaKey.getModulus(), rsaKey.getPublicKey()).getEncoded();
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     */
    public static byte[] encrypt(byte[] data, byte[] publicKey) throws Exception {
        return encryptByPublicKey(data, getPublicKey(publicKey));
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] privateKey)
            throws Exception {
        return decryptByPrivateKey(data, getPrivateKey(privateKey));
    }
}
