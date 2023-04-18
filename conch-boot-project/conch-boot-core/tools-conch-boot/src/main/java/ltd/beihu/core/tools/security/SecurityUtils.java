package ltd.beihu.core.tools.security;

import ltd.beihu.core.tools.utils.Runs;

public class SecurityUtils {

    private static SecurityManager securityManager = SecurityManager.instance();

    @SuppressWarnings("unchecked")
    public static <T, R> Encryptor<T, R> encryptor(String algorithm) {
        Encryptor encryptor = securityManager.encryptor(algorithm);
        return encryptor == null ? null : (input, params) -> (R) encryptor.encrypt(input, params);
    }

    @SuppressWarnings("unchecked")
    public static byte[] hash(byte[] data, HashAlgorithm hashAlgorithm) {
        return Runs.uncheckCall(() -> (byte[]) securityManager.encryptor(hashAlgorithm.getVal())
                .encrypt(data, hashAlgorithm.getVal()));
    }

    public static byte[] md5(byte[] data) {
        return hash(data, HashAlgorithm.MD5);
    }

    @SuppressWarnings("unchecked")
    public static byte[] macHash(byte[] data, HmacAlgorithm hmacAlgorithm, byte[] key) {
        return Runs.uncheckCall(() -> (byte[]) securityManager.encryptor(hmacAlgorithm.getVal())
                .encrypt(data, key, hmacAlgorithm.getVal()));
    }

    public static byte[] macMd5(byte[] data, byte[] key) {
        return macHash(data, HmacAlgorithm.HMACMD5, key);
    }

    /**
     * supported:
     * DES/ECB/PKCS5PADDING,
     * DES/ECB/ISO10126PADDING,
     * DES/CBC/PKCS5PADDING,
     * DES/CBC/ISO10126PADDING,
     * DES/CFB/NOPADDING,
     * DES/CFB/PKCS5PADDING,
     * DES/CFB/ISO10126PADDING,
     * DES/OFB/NOPADDING,
     * DES/OFB/PKCS5PADDING,
     * DES/OFB/ISO10126PADDING,
     * TRIPLE_DES/ECB/PKCS5PADDING,
     * TRIPLE_DES/ECB/ISO10126PADDING,
     * TRIPLE_DES/CBC/PKCS5PADDING,
     * TRIPLE_DES/CBC/ISO10126PADDING,
     * TRIPLE_DES/CFB/NOPADDING,
     * TRIPLE_DES/CFB/PKCS5PADDING,
     * TRIPLE_DES/CFB/ISO10126PADDING,
     * TRIPLE_DES/OFB/NOPADDING,
     * TRIPLE_DES/OFB/PKCS5PADDING,
     * TRIPLE_DES/OFB/ISO10126PADDING,
     * DESEDE/ECB/PKCS5PADDING,
     * DESEDE/ECB/ISO10126PADDING,
     * DESEDE/CBC/PKCS5PADDING,
     * DESEDE/CBC/ISO10126PADDING,
     * DESEDE/CFB/NOPADDING,
     * DESEDE/CFB/PKCS5PADDING,
     * DESEDE/CFB/ISO10126PADDING,
     * DESEDE/OFB/NOPADDING,
     * DESEDE/OFB/PKCS5PADDING,
     * DESEDE/OFB/ISO10126PADDING,
     * AES/ECB/PKCS5PADDING,
     * AES/ECB/ISO10126PADDING,
     * AES/CBC/PKCS5PADDING,
     * AES/CBC/ISO10126PADDING,
     * AES/CFB/NOPADDING,
     * AES/CFB/PKCS5PADDING,
     * AES/CFB/ISO10126PADDING,
     * AES/OFB/NOPADDING,
     * AES/OFB/PKCS5PADDING,
     * AES/OFB/ISO10126PADDING,
     * AES128/ECB/PKCS5PADDING,
     * AES128/ECB/ISO10126PADDING,
     * AES128/CBC/PKCS5PADDING,
     * AES128/CBC/ISO10126PADDING,
     * AES128/CFB/NOPADDING,
     * AES128/CFB/PKCS5PADDING,
     * AES128/CFB/ISO10126PADDING,
     * AES128/OFB/NOPADDING,
     * AES128/OFB/PKCS5PADDING,
     * AES128/OFB/ISO10126PADDING
     * <p>
     * need input length multiple of 8 bytes:
     * DES/ECB/NOPADDING,
     * DES/CBC/NOPADDING,
     * TTRIPLE_DES/ECB/NOPADDING,
     * TTRIPLE_DES/CBC/NOPADDING,
     * DESEDE/ECB/NOPADDING,
     * DESEDE/CBC/NOPADDING
     * <p>
     * need input length multiple of 16 bytes:
     * AES/ECB/NOPADDING,
     * AES/CBC/NOPADDING
     * AES128/ECB/NOPADDING,
     * AES128/CBC/NOPADDING
     * <p>
     *
     * @param data
     * @param desAlgorithm
     * @param key
     * @param mode
     * @param padding
     * @return
     * @see EsAlgorithm#AES256
     */
    @SuppressWarnings("unchecked")
    public static byte[] es(byte[] data, EsAlgorithm desAlgorithm, byte[] key, EsAlgorithm.Mode mode, EsAlgorithm.PADDING padding) {
        return Runs.uncheckCall(() -> (byte[]) securityManager.encryptor(desAlgorithm.getVal())
                .encrypt(data, key, desAlgorithm.getVal(), mode.name(), padding.getVal()));
    }

    @SuppressWarnings("unchecked")
    public static byte[] es(byte[] data, EsAlgorithm desAlgorithm, byte[] key, EsAlgorithm.Mode mode) {
        return es(data, desAlgorithm, key, mode, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    @SuppressWarnings("unchecked")
    public static byte[] es(byte[] data, EsAlgorithm desAlgorithm, byte[] key) {
        return es(data, desAlgorithm, key, EsAlgorithm.Mode.ECB, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    @SuppressWarnings("unchecked")
    public static byte[] des(byte[] data, byte[] key, EsAlgorithm.Mode mode, EsAlgorithm.PADDING padding) {
        return es(data, EsAlgorithm.DES, key, mode, padding);
    }

    @SuppressWarnings("unchecked")
    public static byte[] des(byte[] data, byte[] key, EsAlgorithm.Mode mode) {
        return es(data, EsAlgorithm.DES, key, mode, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    @SuppressWarnings("unchecked")
    public static byte[] des(byte[] data, byte[] key) {
        return es(data, EsAlgorithm.DES, key, EsAlgorithm.Mode.ECB, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    /**
     * @param data    data to encrypt
     * @param key     key for aes
     * @param mode    recommend：@{code EsAlgorithm.Mode.ECB}
     * @param padding recommend：@{code EsAlgorithm.PADDING.PKCS5PADDING}
     * @return encrypt data
     */
    @SuppressWarnings("unchecked")
    public static byte[] aes(byte[] data, byte[] key, EsAlgorithm.Mode mode, EsAlgorithm.PADDING padding) {
        return es(data, EsAlgorithm.AES, key, mode, padding);
    }

    @SuppressWarnings("unchecked")
    public static byte[] aes(byte[] data, byte[] key, EsAlgorithm.Mode mode) {
        return es(data, EsAlgorithm.AES, key, mode, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    @SuppressWarnings("unchecked")
    public static byte[] aes(byte[] data, byte[] key) {
        return es(data, EsAlgorithm.AES, key, EsAlgorithm.Mode.ECB, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    /**
     * supported:
     * DES/ECB/PKCS5PADDING,
     * DES/ECB/ISO10126PADDING,
     * DES/CBC/PKCS5PADDING,
     * DES/CBC/ISO10126PADDING,
     * DES/CFB/NOPADDING,
     * DES/CFB/PKCS5PADDING,
     * DES/CFB/ISO10126PADDING,
     * DES/OFB/NOPADDING,
     * DES/OFB/PKCS5PADDING,
     * DES/OFB/ISO10126PADDING,
     * TRIPLE_DES/ECB/PKCS5PADDING,
     * TRIPLE_DES/ECB/ISO10126PADDING,
     * TRIPLE_DES/CBC/PKCS5PADDING,
     * TRIPLE_DES/CBC/ISO10126PADDING,
     * TRIPLE_DES/CFB/NOPADDING,
     * TRIPLE_DES/CFB/PKCS5PADDING,
     * TRIPLE_DES/CFB/ISO10126PADDING,
     * TRIPLE_DES/OFB/NOPADDING,
     * TRIPLE_DES/OFB/PKCS5PADDING,
     * TRIPLE_DES/OFB/ISO10126PADDING,
     * DESEDE/ECB/PKCS5PADDING,
     * DESEDE/ECB/ISO10126PADDING,
     * DESEDE/CBC/PKCS5PADDING,
     * DESEDE/CBC/ISO10126PADDING,
     * DESEDE/CFB/NOPADDING,
     * DESEDE/CFB/PKCS5PADDING,
     * DESEDE/CFB/ISO10126PADDING,
     * DESEDE/OFB/NOPADDING,
     * DESEDE/OFB/PKCS5PADDING,
     * DESEDE/OFB/ISO10126PADDING,
     * AES/ECB/PKCS5PADDING,
     * AES/ECB/ISO10126PADDING,
     * AES/CBC/PKCS5PADDING,
     * AES/CBC/ISO10126PADDING,
     * AES/CFB/NOPADDING,
     * AES/CFB/PKCS5PADDING,
     * AES/CFB/ISO10126PADDING,
     * AES/OFB/NOPADDING,
     * AES/OFB/PKCS5PADDING,
     * AES/OFB/ISO10126PADDING,
     * AES128/ECB/PKCS5PADDING,
     * AES128/ECB/ISO10126PADDING,
     * AES128/CBC/PKCS5PADDING,
     * AES128/CBC/ISO10126PADDING,
     * AES128/CFB/NOPADDING,
     * AES128/CFB/PKCS5PADDING,
     * AES128/CFB/ISO10126PADDING,
     * AES128/OFB/NOPADDING,
     * AES128/OFB/PKCS5PADDING,
     * AES128/OFB/ISO10126PADDING
     * <p>
     * need input length multiple of 8 bytes:
     * DES/ECB/NOPADDING,
     * DES/CBC/NOPADDING,
     * TTRIPLE_DES/ECB/NOPADDING,
     * TTRIPLE_DES/CBC/NOPADDING,
     * DESEDE/ECB/NOPADDING,
     * DESEDE/CBC/NOPADDING
     * <p>
     * need input length multiple of 16 bytes:
     * AES/ECB/NOPADDING,
     * AES/CBC/NOPADDING
     * AES128/ECB/NOPADDING,
     * AES128/CBC/NOPADDING
     * <p>
     *
     * @param data
     * @param desAlgorithm
     * @param key
     * @param mode
     * @param padding
     * @return
     * @see EsAlgorithm#AES256
     */
    @SuppressWarnings("unchecked")
    public static byte[] esDecrypt(byte[] data, EsAlgorithm desAlgorithm, byte[] key, EsAlgorithm.Mode mode, EsAlgorithm.PADDING padding) {
        return Runs.uncheckCall(() -> (byte[]) securityManager.decryptor(desAlgorithm.getVal())
                .decrypt(data, key, desAlgorithm.getVal(), mode.name(), padding.getVal()));
    }

    @SuppressWarnings("unchecked")
    public static byte[] esDecrypt(byte[] data, EsAlgorithm desAlgorithm, byte[] key, EsAlgorithm.Mode mode) {
        return esDecrypt(data, desAlgorithm, key, mode, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    @SuppressWarnings("unchecked")
    public static byte[] esDecrypt(byte[] data, EsAlgorithm desAlgorithm, byte[] key) {
        return esDecrypt(data, desAlgorithm, key, EsAlgorithm.Mode.ECB, EsAlgorithm.PADDING.PKCS5PADDING);
    }

    /**
     * @param data    data to decrypt
     * @param key     key for des
     * @param mode    recommend：@{code EsAlgorithm.Mode.ECB}
     * @param padding recommend：@{code EsAlgorithm.PADDING.PKCS5PADDING}
     * @return decrypt data
     */
    @SuppressWarnings("unchecked")
    public static byte[] desDecrypt(byte[] data, byte[] key, EsAlgorithm.Mode mode, EsAlgorithm.PADDING padding) {
        return esDecrypt(data, EsAlgorithm.DES, key, mode, padding);
    }

    /**
     * @param data    data to decrypt
     * @param key     key for aes
     * @param mode    recommend：@{code EsAlgorithm.Mode.ECB}
     * @param padding recommend：@{code EsAlgorithm.PADDING.PKCS5PADDING}
     * @return decrypt data
     */
    @SuppressWarnings("unchecked")
    public static byte[] aesDecrypt(byte[] data, byte[] key, EsAlgorithm.Mode mode, EsAlgorithm.PADDING padding) {
        return esDecrypt(data, EsAlgorithm.AES, key, mode, padding);
    }

    @SuppressWarnings("unchecked")
    public static byte[] mix(byte[] data) {
        return Runs.uncheckCall(() -> (byte[]) securityManager.encryptor("mix")
                .encrypt(data));
    }

    @SuppressWarnings("unchecked")
    public static byte[] unmix(byte[] data) {
        return Runs.uncheckCall(() -> (byte[]) securityManager.decryptor("mix")
                .decrypt(data));
    }

    /**
     * rsa公钥加密
     */
    @SuppressWarnings("unchecked")
    public static byte[] rsa(byte[] data, byte[] publicKey) throws Exception {
        return Runs.uncheckCall(() -> (byte[]) securityManager.decryptor("rsa")
                .decrypt(data, (Object) publicKey));
    }

    /**
     * rsa私钥解密
     */
    @SuppressWarnings("unchecked")
    public static byte[] rsaDecrypt(byte[] data, byte[] privateKey)
            throws Exception {
        return Runs.uncheckCall(() -> (byte[]) securityManager.decryptor("rsa")
                .decrypt(data, (Object) privateKey));
    }

}
