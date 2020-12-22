package com.example.demo.user;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@Slf4j
public class UserCryptoService {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    private final SecretKeySpec secretKey;
    private final IvParameterSpec ivParameter;

    public UserCryptoService(byte[] key, byte[] iv) {
        if (key.length != 32) {
            throw new IllegalArgumentException("Key length must be 32 bytes");
        }
        if (iv.length != 16) {
            throw new IllegalArgumentException("IV length must be 16 bytes");
        }

        this.secretKey = new SecretKeySpec(key, AES_ALGORITHM);
        this.ivParameter = new IvParameterSpec(iv);
    }

    String encrypt(String str) {
        byte[] value = str.getBytes();
        byte[] encryptedValue = encrypt(value);

        String encrypted = Base64.getEncoder().encodeToString(encryptedValue);
        log.debug("=== encryption: {}", encrypted);
        return encrypted;
    }

    String decrypt(String cipherStr) {
        byte[] encryptedValue = Base64.getDecoder().decode(cipherStr);
        byte[] value = decrypt(encryptedValue);

        String decrypted = new String(value);
        log.debug("=== decryption: {}", decrypted);
        return decrypted;
    }

    private byte[] encrypt(byte[] input) {
        Cipher cipher = createCipher();
        initCipher(cipher, Cipher.ENCRYPT_MODE);
        return doFinal(cipher, input);
    }

    private byte[] decrypt(byte[] input) {
        Cipher cipher = createCipher();
        initCipher(cipher, Cipher.DECRYPT_MODE);
        return doFinal(cipher, input);
    }

    private Cipher createCipher() {
        try {
            return Cipher.getInstance(AES_CBC_PKCS5PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            throw new RuntimeException("Unexpected cryptography exception", ex);
        }
    }

    private void initCipher(Cipher cipher, int opmode) {
        try {
            cipher.init(opmode, secretKey, ivParameter);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException ex) {
            throw new RuntimeException("Crypto configuration exception", ex);
        }
    }

    private byte[] doFinal(Cipher cipher, byte[] input) {
        try {
            return cipher.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException("Crypto exception", ex);
        }
    }

}
