package com.i9930.croptrails;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils2 {
    private static String TAG="AESUtils2";
    private static final String pass="crptrlssword";
    private static String AES="AES";


    public static String encrypt(String string) throws Exception{
        SecretKeySpec key=generateKey(pass);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[]bytes=c.doFinal(string.getBytes());
        String encString= Base64.encodeToString(bytes,Base64.DEFAULT);
        Log.e(TAG,"Enc From "+string+" To "+encString);
        return encString;
    }

    public static String decrypt(String string) throws Exception{
        SecretKeySpec key=generateKey(pass);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[]bytes= Base64.decode(string,Base64.DEFAULT);
        byte[]decVal=c.doFinal(bytes);
        String decreptedVal=new String(decVal);
        Log.e(TAG,"Dec From "+string+" To "+decreptedVal);
        return decreptedVal;
    }
    private static SecretKeySpec generateKey(String pass) throws Exception{
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[]bytes=pass.getBytes();
        digest.update(bytes,0,bytes.length);
        byte[]key=digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
}
