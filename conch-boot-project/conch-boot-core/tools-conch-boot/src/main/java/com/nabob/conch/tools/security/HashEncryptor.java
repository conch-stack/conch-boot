package com.nabob.conch.tools.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@EncryptSupport({"MD5", "SHA", "SHA1", "SHA-256", "SHA-512"})
public class HashEncryptor {

    public static byte[] encrypt(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        if (data == null) return null;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(data);
        return md.digest();
    }

}
