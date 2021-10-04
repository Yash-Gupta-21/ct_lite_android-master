package com.i9930.croptrails;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Jane on 2018/8/8.
 * AES CRT mode encryption tools
 */
public class AESCrtUtils {
    /*
     * AES CRT encryption
     * @Param content to be encrypted content
     * @Param key encryption keys file
     * @Param iv encrypted offset
     * @Return output Hex Hex re-encrypted results
     **/
    private static String key = "MIICCgKCAgEAt954OSnRpX4ZMuHCjEvCpSUJvi2VkKhOoBpWIApeCX7js33BDV51\n" +
            "pM/KZX78xmF0av4ZDKnuPWzckK4E55Igx/+5Q8+zKn3WhfsJ8EXFUK3aR9nQUXVg\n" +
            "eBbr1icWlXEThzHgRrACjRgMuRG1shyLI/EeHNtl3oqZTfodJv91Ov+rteO/D05n\n" +
            "GFgqYDfstQnuOFbdWh9zXty9wZzYrYq3tItOYCc0WvXGstH7gDkvoUzJJU7lvhIA\n" +
            "as1UZOr7Vs26h7JeP0mS13QrnQHaSoS7UHrJEy8wi+ufhH3cno4aaD+v3YlW7+Qw\n" +
            "eKeqFwLeVWJtjnFnY1CDpT/V66Zar4A4AnzvNWRdmn63lM/luddldEFF3UVAikXb\n" +
            "bpf41Bkgl/qyR69d4GfpO05CRBCsgLulrckFkqNVsipxNhZSO+PCVxIyPs0Uas8I\n" +
            "6Km9h7CQzgdXqypNZpF+fk37EzFG/LfGyrOfL1DI4oDCDrpALohgjHwzQwFIqk8u\n" +
            "MEPAhaEpczRzRWVdBdjIRvdLFm2qrXXVDRSC6/a+fqCm0M+bnVh72vREg8D0U2NV\n" +
            "++0gC8IYFfBiXDRPI8LBYMt5W9JwCUi0VC5RArgu0qgcVoEYDs/BYUhZJl9mVk2U\n" +
            "2wn0QWPXCl0PUbqmvN8+x/3DcNxvsr8dMskp/b+Fe7OT6tvBqSJiD68CAwEAAQ==";

    public static String encryptA(String src) {
        try {

            PublicKey key = getKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] ciphertext = cipher.doFinal(src.getBytes());
            byte[] iv = cipher.getIV();

            Log.d("AESCrtUtils", "11before: " + src + ", after: " + new String(ciphertext));
            return Base64.encodeToString(cipher.doFinal(src.getBytes()),0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static PublicKey getKey(){
        try{
            byte[] byteKey =key.getBytes();
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    static Key makeKey() {
        try {
            byte[] keyy = key.substring(0,256).getBytes("UTF-8");
            return new SecretKeySpec(keyy, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String encryptRSAToString(String text) {
        byte[] cipherText = null;
        String strEncryInfoData = "";
        try {

            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(key.trim().getBytes(), Base64.DEFAULT));
            Key publicKey = keyFac.generatePublic(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherText = cipher.doFinal(text.getBytes());
            strEncryInfoData = new String(Base64.encode(cipherText, Base64.DEFAULT));

        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = strEncryInfoData.replaceAll("(\\r|\\n)", "");

        Log.d("AESCrtUtils", "1before: " + text + ", after: " + s);
        return s;
    }

    public static String encrypt(String content) {
        String encodeStr = "";
        Log.d("AESCrtUtils", key.substring(0, 256) + "\nKEY LENGTH " + key.substring(0, 256).length());
        try {
            // generated key
            byte[] keyBytes = key.substring(0, 256).getBytes();
            // build SecretKeySpec, this parameter is required when Cipher object is initialized
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            // build Cipher object, need to pass a string format must be "algorithm / mode encryption / filling mode" for the "algorithm / mode / padding" or "algorithm /", meaning
            Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
            // initialize the Cipher object


            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // encrypted data
            byte[] resultBytes = cipher.doFinal(content.getBytes());
            // results with Hex Hex transcoding
            encodeStr = new String(resultBytes);
            return encodeStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        Log.d("AESCrtUtils", "before: " + content + ", after: " + encodeStr);
        return encodeStr;
    }

    /**
     * AES CTR decryption
     *
     * @Param encode the encrypted file after the
     * @Param iv offset
     * @Param key keys file
     */
    public static SecretKeySpec generateKey(String pass) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = pass.getBytes();
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    public static String decryptt(String string) throws Exception {
        SecretKeySpec key = generateKey("9d15065bd5a2c0abae550873b0c7bf1470081573582b7d51bf52bec00c18f835");
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        byte[] decVal = c.doFinal(bytes);
        String decreptedVal = new String(decVal);
        Log.e("ENC FARM ", "Dec From " + string + " To " + decreptedVal);
        return decreptedVal;
    }

    public static String decrypt(String encode) {
        return decrypt(encode, "00000000");
    }

    private static String decrypt(String encode, String iv) {
        String decoded = "";


        try {
            byte[] bytes = encode.getBytes();
            IvParameterSpec ivSpec = new IvParameterSpec(
                    iv.toString().getBytes());
            Key keys = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, keys, ivSpec);
            byte[] ret = cipher.doFinal(bytes);
            decoded = new String(ret);
            return decoded.trim();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return decoded;
    }


}