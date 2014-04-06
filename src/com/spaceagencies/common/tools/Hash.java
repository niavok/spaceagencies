
package com.spaceagencies.common.tools;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Hash {
    // Be ware that it may broke the db if the value is changed.
    private final static int ITERATION_NUMBER = 1000;
    private static final int RANDOM_SIZE = 512;
    private static final String SHA1_PRNG = "SHA1PRNG";

    /**
     * From a password and a salt, returns the corresponding digest
     * 
     * @param password The password to encrypt
     * @param salt The salt
     * @return String The digested password in base64
     */
    public static String calculateHash(final String password, final String salt) {
        byte[] value = (password + salt).getBytes();
        for (int i = 0; i < ITERATION_NUMBER; i++) {
            value = DigestUtils.sha512(value);
        }
        return Base64.encodeBase64String(value);
    }

    public static String shortHash(final String value) {
        return DigestUtils.sha512Hex(value).substring(10, 20);
    }

    public static String generateUniqueToken(int size) {
        return generateUniqueToken().substring(0, size);
    }

    public static String generateUniqueToken() {
        UUID.randomUUID().toString();
        try {
            final byte[] bytes = new byte[RANDOM_SIZE];
            SecureRandom.getInstance(SHA1_PRNG).nextBytes(bytes);
            return DigestUtils.sha512Hex(DigestUtils.sha512Hex(bytes) + UUID.randomUUID().toString());
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
