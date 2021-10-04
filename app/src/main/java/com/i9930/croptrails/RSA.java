package com.i9930.croptrails;

import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    private static String TAG = "RSA";
    static KeyPairGenerator kpg;
    static  KeyPair kp;
    static PublicKey publicKey;
    static PrivateKey privateKey;
    static byte[] encryptedBytes, decryptedBytes;
    static Cipher cipher, cipher1;
    static String encrypted, decrypted;

    public static String Encrypt(String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(4096);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());

        encrypted = bytesToString(encryptedBytes);


        Log.e(TAG, "before: " + plain + ", after: " + encrypted);

        return encrypted;

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static  String Decrypt(String result) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {

        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(
                "5kQaVSoYtXTYSwWq4waP3JCR32yl9yK2pM472pI4xLUqH4yRxfvwzm2WicUhQn0e\n" +
                        "Fl912Q7DHbt/0nHX5IFljYcYux89AoKxe8xhIRZ/TXrYqBHXItrOdiK4PngPXSSL\n" +
                        "3HhSpefrEb+/utaCILJG29LqfR5oPz71SVIyX7vtg1prSdrcW4nGrToBgbYQ8ujV\n" +
                        "zhxgw8Fa8wuJ2dqd3d8SFuPf5CBZjrSeZVd7/b/g/t/Zwbe6rA086qqJLS1akqlo\n" +
                        "Sk2zYvQGz0yWladMefYU26S1gXh4XTshLoTHyCrA/JuIjTedw+TefDBtVR7bnfJP\n" +
                        "RU8l/Px3FSN6wuie3cSeLjasvKnYudBXHWCaAH2ee4aN7pK73BcVvwBWVuNnzsyO\n" +
                        "pSNLTRnswe2nVzoRvOSeH6F5cvF0/cOfi8FtyhASo6LJji288GAM9OSLcCgLzKSL\n" +
                        "P/067678zOXgCkWyixhM+KpPfM+AygcdNBoBrl9brgnuZ2nxmzErqnAQgvOmt84/\n" +
                        "uTKnGKIYncZGEvqn+Ou3sDmHshaHrWSMphkRY7KfBgP6fYYDmosAGAaLfITURvGX\n" +
                        "ubQ4stI+lBU2dPVHFi/5vkUBpO0AbVB6PO7hdKhd9B+60tE5FSkmoSVgM9U6m0JY\n" +
                        "yNGOvFeBlPwPUvvKsSQSW4Dzib+Y45FmN/HGFrHMOYPfNfYove6nWJF9Tf77uQH9\n" +
                        "n36u3Pz6DsPjTtAHMvjc7elF2HIN4KzibFJOGwYLjla4SACh4nffzaAysy5zaw7g\n" +
                        "qLNzVrgCDOYmOguFng9/QMPz85DnbbdmndLpYSvNkuBcUGMkuYDa2+hOALsgIuoT\n" +
                        "ow8nhUD/yFyCDQM7IIJS63f/kRMl/qaM65KajooBv3XWFXkNsyP+va+B1f7W6M3v\n" +
                        "b5m+/EZgML04YTiipcaetfX2/n4pHyjCUexzJOiUa7sG6pg6BkdwHi5j/j7qvswh\n" +
                        "/T3CgfVzm4iAHvYb6KoJXNQhJkoIyAonYXhZFwPBErb+gsLkiOm99/I6uvMRqBVp\n" +
                        "GHsb5Yx/E5TVd9Jwsk1igma0cJzgufftTyiefiVFn1rFSB2kSL9WPEuDOqdaslWG\n" +
                        "XPxBuHWxC/ALSSIutBfb9kV2UYo90uBfseo1mZrj3qPrdcKZXdwG5M+fOMylHQbQ\n" +
                        "2aOTob1hxfXB6U5tLoDoI8VJvC0ZBB7JPJNWnUz8+uBtDJfThdcDTuq0zOiCP3FF\n" +
                        "1LUzcb8ikL2DXNfC3qe1TEg5EWE0QMG0H59DuDxuDD73cxzqxEUapK3edibvfHmN\n" +
                        "lBVmvvzMNsDcKLVyPQ/DhQFl2xS00cH9DSlchDKPLALhkrbtIQV1xI8VuNF8hDvq\n" +
                        "L5VVHeE949GgLY2asmcrPgleXCwIBSuVWQMlSm16EPaMcnXV9vI49aqjgqrpNzzQ\n" +
                        "YGQtEgsXj06lLY6ySdnABmVGrLAywrrh/TjNdIp3yqG98BNpXqM20wR6ggMkf7XX\n" +
                        "FSly10bQ/ApCXHf1a+GSefA0NAO2uU0GQRJmo95CdV2vwfQqycdVCfePRndsCUOH\n" +
                        "uO1AGjIcUjv97INH9od4hkrN5JFhhhYVrPqLlrfUsEQAaDNg+Wdx8FrlkImlSny+\n" +
                        "dn+bH3OWqhD9i+y7hAX4Z6dCGId/Ct4Oytv0Oo48lhYZnukW1zAicgvruM4rBTmn\n" +
                        "GBUWHNX1gS8kSQ+gMit3GUmAtTPOQ4RNV8PHkPbcyOPUHIdTW6rZAuGiwhM/wwXP\n" +
                        "WcuV4awvbH3rC53FVxrgE5J/9YnkMHPPfn5WCz7I3/2VozDrNbxEPTTVo0r42nW/\n" +
                        "ZvW6POzIjrrYEjtzkdfZS7ar1vHo+LsmzzsJJ1L0nOZwprqFxg09860whikkz9+K\n" +
                        "Mt0UsHuIhWm8077kwEtv8NLrHxAqLkBxf8wVeQGCsw8eMyAQFPPa5bQneUaQck4F\n" +
                        "vwG1ekxJT43vHMk8MTzKkClhn50s6EnVAqJsRXrmEr2CQeZpuSaATCOZMB1gWfHi\n" +
                        "PXoH3PI85zoyqRuuO/pHAbsCi1hInXavnodJaKvZxtIOItAna8+6c8Jbn35GvW8E\n" +
                        "+gtxpJbhOSSB2Dt+eh49jlhUnOPOa0BJwg/uyxSzMLk4CRNszVk/QPXBh+kqH5XF\n" +
                        "qsqMqweVk1jS3dC/EMh1fKa3k0sdFxmmCy47hxw+HexUeWR8DGtiCqz6bhVTBDSB\n" +
                        "ZAphngxJ/vAdYkNAE8k/XyuPYFM4aJ0GXrBbO+Hxy3BhzRYhs0f24KmtivKw5uGr\n" +
                        "BpdX8Nm6QIS5HCd16aiVOF7YqpnmfUdo9TKGJqvN+6jj4aWgHUrkaT0Gw8x1YMHz\n" +
                        "bd5rCb+UvUPYG6FcWrK0d/8SyIyS8SoJU7CT9Fahl68JueAjPO4RqHM2/fJBI+TC\n" +
                        "/+M2O+iVm3MzcywTHxZo8hBA0/49/ldujih4ENA+3+C5PODypW3UzIhalJELQJZE\n" +
                        "itXxemGc14IX58y81uggWSxyZXqHMhKtErR4cTWclHqbXMoPMjqhD7kfCYDKP+z8\n" +
                        "f1KNXU8RMqdEaX3ZBUQ9em34pEqsnoH+rjvT2RxbhccWPK9BgUIR11kJTtnbQiBO\n" +
                        "C4YKreA8LeRHJsNFrp9fUU9YANUHxRmSJOM06eswu4Nx+cQN3y5t0Za5fzrr94RL\n" +
                        "yU5VokQVQoQZEN3y8VY4fdLhLgR3GCQMCsp9MGeB26IxAy1cgneJTf0mKar3FDaG\n" +
                        "bgZFGnQotcPOKjXBBXE1wf0Oo2H3ZxuS6wcS8MFI8qCsJqwmbeQYtb9CveiY2l1S\n" +
                        "BzecaSsvJ9io74zSxnvHvwC/wOfDSbHA8lZp/fFzwFAhjcytCHs+M2G3Wkf0LU8c\n" +
                        "kCYOioJPgY1WL9TZVphuNFHCiOu3Nkqv4XGSBaBddnPmXsisD69a7nnI4YX11Npo\n" +
                        "AYPj+eHFCTivcgZenrUYd+03VunxlWfua7ffOqalnWt89F6CingZzULZz4jltj3Y\n" +
                        "sMbLslrdxxZ3YyM9D02I6Vi39H0fHnAeWUqh3HukDIymFL7WkFTtvhKgi2152iK8\n" +
                        "MViKGGGFP+Q4dCH/Lfz7HucfBUZnDW0cSE60Z5xgGWABeNizOvVvCpiGah9RM55l\n" +
                        "xQ3NCamaHtWvMUUTSOqe9H9DQgRwijViDNCdG27qPIaKyiLYwW6bumfKJUbG3eZ9\n"));


        String privateKeyContent =
                "5kQaVSoYtXTYSwWq4waP3JCR32yl9yK2pM472pI4xLUqH4yRxfvwzm2WicUhQn0e\n" +
                        "Fl912Q7DHbt/0nHX5IFljYcYux89AoKxe8xhIRZ/TXrYqBHXItrOdiK4PngPXSSL\n" +
                        "3HhSpefrEb+/utaCILJG29LqfR5oPz71SVIyX7vtg1prSdrcW4nGrToBgbYQ8ujV\n" +
                        "zhxgw8Fa8wuJ2dqd3d8SFuPf5CBZjrSeZVd7/b/g/t/Zwbe6rA086qqJLS1akqlo\n" +
                        "Sk2zYvQGz0yWladMefYU26S1gXh4XTshLoTHyCrA/JuIjTedw+TefDBtVR7bnfJP\n" +
                        "RU8l/Px3FSN6wuie3cSeLjasvKnYudBXHWCaAH2ee4aN7pK73BcVvwBWVuNnzsyO\n" +
                        "pSNLTRnswe2nVzoRvOSeH6F5cvF0/cOfi8FtyhASo6LJji288GAM9OSLcCgLzKSL\n" +
                        "P/067678zOXgCkWyixhM+KpPfM+AygcdNBoBrl9brgnuZ2nxmzErqnAQgvOmt84/\n" +
                        "uTKnGKIYncZGEvqn+Ou3sDmHshaHrWSMphkRY7KfBgP6fYYDmosAGAaLfITURvGX\n" +
                        "ubQ4stI+lBU2dPVHFi/5vkUBpO0AbVB6PO7hdKhd9B+60tE5FSkmoSVgM9U6m0JY\n" +
                        "yNGOvFeBlPwPUvvKsSQSW4Dzib+Y45FmN/HGFrHMOYPfNfYove6nWJF9Tf77uQH9\n" +
                        "n36u3Pz6DsPjTtAHMvjc7elF2HIN4KzibFJOGwYLjla4SACh4nffzaAysy5zaw7g\n" +
                        "qLNzVrgCDOYmOguFng9/QMPz85DnbbdmndLpYSvNkuBcUGMkuYDa2+hOALsgIuoT\n" +
                        "ow8nhUD/yFyCDQM7IIJS63f/kRMl/qaM65KajooBv3XWFXkNsyP+va+B1f7W6M3v\n" +
                        "b5m+/EZgML04YTiipcaetfX2/n4pHyjCUexzJOiUa7sG6pg6BkdwHi5j/j7qvswh\n" +
                        "/T3CgfVzm4iAHvYb6KoJXNQhJkoIyAonYXhZFwPBErb+gsLkiOm99/I6uvMRqBVp\n" +
                        "GHsb5Yx/E5TVd9Jwsk1igma0cJzgufftTyiefiVFn1rFSB2kSL9WPEuDOqdaslWG\n" +
                        "XPxBuHWxC/ALSSIutBfb9kV2UYo90uBfseo1mZrj3qPrdcKZXdwG5M+fOMylHQbQ\n" +
                        "2aOTob1hxfXB6U5tLoDoI8VJvC0ZBB7JPJNWnUz8+uBtDJfThdcDTuq0zOiCP3FF\n" +
                        "1LUzcb8ikL2DXNfC3qe1TEg5EWE0QMG0H59DuDxuDD73cxzqxEUapK3edibvfHmN\n" +
                        "lBVmvvzMNsDcKLVyPQ/DhQFl2xS00cH9DSlchDKPLALhkrbtIQV1xI8VuNF8hDvq\n" +
                        "L5VVHeE949GgLY2asmcrPgleXCwIBSuVWQMlSm16EPaMcnXV9vI49aqjgqrpNzzQ\n" +
                        "YGQtEgsXj06lLY6ySdnABmVGrLAywrrh/TjNdIp3yqG98BNpXqM20wR6ggMkf7XX\n" +
                        "FSly10bQ/ApCXHf1a+GSefA0NAO2uU0GQRJmo95CdV2vwfQqycdVCfePRndsCUOH\n" +
                        "uO1AGjIcUjv97INH9od4hkrN5JFhhhYVrPqLlrfUsEQAaDNg+Wdx8FrlkImlSny+\n" +
                        "dn+bH3OWqhD9i+y7hAX4Z6dCGId/Ct4Oytv0Oo48lhYZnukW1zAicgvruM4rBTmn\n" +
                        "GBUWHNX1gS8kSQ+gMit3GUmAtTPOQ4RNV8PHkPbcyOPUHIdTW6rZAuGiwhM/wwXP\n" +
                        "WcuV4awvbH3rC53FVxrgE5J/9YnkMHPPfn5WCz7I3/2VozDrNbxEPTTVo0r42nW/\n" +
                        "ZvW6POzIjrrYEjtzkdfZS7ar1vHo+LsmzzsJJ1L0nOZwprqFxg09860whikkz9+K\n" +
                        "Mt0UsHuIhWm8077kwEtv8NLrHxAqLkBxf8wVeQGCsw8eMyAQFPPa5bQneUaQck4F\n" +
                        "vwG1ekxJT43vHMk8MTzKkClhn50s6EnVAqJsRXrmEr2CQeZpuSaATCOZMB1gWfHi\n" +
                        "PXoH3PI85zoyqRuuO/pHAbsCi1hInXavnodJaKvZxtIOItAna8+6c8Jbn35GvW8E\n" +
                        "+gtxpJbhOSSB2Dt+eh49jlhUnOPOa0BJwg/uyxSzMLk4CRNszVk/QPXBh+kqH5XF\n" +
                        "qsqMqweVk1jS3dC/EMh1fKa3k0sdFxmmCy47hxw+HexUeWR8DGtiCqz6bhVTBDSB\n" +
                        "ZAphngxJ/vAdYkNAE8k/XyuPYFM4aJ0GXrBbO+Hxy3BhzRYhs0f24KmtivKw5uGr\n" +
                        "BpdX8Nm6QIS5HCd16aiVOF7YqpnmfUdo9TKGJqvN+6jj4aWgHUrkaT0Gw8x1YMHz\n" +
                        "bd5rCb+UvUPYG6FcWrK0d/8SyIyS8SoJU7CT9Fahl68JueAjPO4RqHM2/fJBI+TC\n" +
                        "/+M2O+iVm3MzcywTHxZo8hBA0/49/ldujih4ENA+3+C5PODypW3UzIhalJELQJZE\n" +
                        "itXxemGc14IX58y81uggWSxyZXqHMhKtErR4cTWclHqbXMoPMjqhD7kfCYDKP+z8\n" +
                        "f1KNXU8RMqdEaX3ZBUQ9em34pEqsnoH+rjvT2RxbhccWPK9BgUIR11kJTtnbQiBO\n" +
                        "C4YKreA8LeRHJsNFrp9fUU9YANUHxRmSJOM06eswu4Nx+cQN3y5t0Za5fzrr94RL\n" +
                        "yU5VokQVQoQZEN3y8VY4fdLhLgR3GCQMCsp9MGeB26IxAy1cgneJTf0mKar3FDaG\n" +
                        "bgZFGnQotcPOKjXBBXE1wf0Oo2H3ZxuS6wcS8MFI8qCsJqwmbeQYtb9CveiY2l1S\n" +
                        "BzecaSsvJ9io74zSxnvHvwC/wOfDSbHA8lZp/fFzwFAhjcytCHs+M2G3Wkf0LU8c\n" +
                        "kCYOioJPgY1WL9TZVphuNFHCiOu3Nkqv4XGSBaBddnPmXsisD69a7nnI4YX11Npo\n" +
                        "AYPj+eHFCTivcgZenrUYd+03VunxlWfua7ffOqalnWt89F6CingZzULZz4jltj3Y\n" +
                        "sMbLslrdxxZ3YyM9D02I6Vi39H0fHnAeWUqh3HukDIymFL7WkFTtvhKgi2152iK8\n" +
                        "MViKGGGFP+Q4dCH/Lfz7HucfBUZnDW0cSE60Z5xgGWABeNizOvVvCpiGah9RM55l\n" +
                        "xQ3NCamaHtWvMUUTSOqe9H9DQgRwijViDNCdG27qPIaKyiLYwW6bumfKJUbG3eZ9\n";
//        String publicKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("public_key.pem").toURI())));

        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
//        publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");;

        KeyFactory kf = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

//        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
//        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

        System.out.println(privKey);
//        System.out.println(pubKey);

        System.out.println(privKey);
        cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privKey);
        decryptedBytes = cipher1.doFinal(stringToBytes(result));
        decrypted = new String(decryptedBytes);

        Log.e(TAG, "before: " + result + ", after: " + decrypted);
        return decrypted;

    }

    public static  String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    public static  byte[] stringToBytes(String s) {
        byte[] b2 = new BigInteger(s, 36).toByteArray();
        return Arrays.copyOfRange(b2, 1, b2.length);
    }



    private static final int MAX_DECRYPT_BLOCK = 1028;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] decryptByPrivateKey(byte[] encryptedData) throws Exception {


        String privateKeyContent =
                "5kQaVSoYtXTYSwWq4waP3JCR32yl9yK2pM472pI4xLUqH4yRxfvwzm2WicUhQn0e\n" +
                        "Fl912Q7DHbt/0nHX5IFljYcYux89AoKxe8xhIRZ/TXrYqBHXItrOdiK4PngPXSSL\n" +
                        "3HhSpefrEb+/utaCILJG29LqfR5oPz71SVIyX7vtg1prSdrcW4nGrToBgbYQ8ujV\n" +
                        "zhxgw8Fa8wuJ2dqd3d8SFuPf5CBZjrSeZVd7/b/g/t/Zwbe6rA086qqJLS1akqlo\n" +
                        "Sk2zYvQGz0yWladMefYU26S1gXh4XTshLoTHyCrA/JuIjTedw+TefDBtVR7bnfJP\n" +
                        "RU8l/Px3FSN6wuie3cSeLjasvKnYudBXHWCaAH2ee4aN7pK73BcVvwBWVuNnzsyO\n" +
                        "pSNLTRnswe2nVzoRvOSeH6F5cvF0/cOfi8FtyhASo6LJji288GAM9OSLcCgLzKSL\n" +
                        "P/067678zOXgCkWyixhM+KpPfM+AygcdNBoBrl9brgnuZ2nxmzErqnAQgvOmt84/\n" +
                        "uTKnGKIYncZGEvqn+Ou3sDmHshaHrWSMphkRY7KfBgP6fYYDmosAGAaLfITURvGX\n" +
                        "ubQ4stI+lBU2dPVHFi/5vkUBpO0AbVB6PO7hdKhd9B+60tE5FSkmoSVgM9U6m0JY\n" +
                        "yNGOvFeBlPwPUvvKsSQSW4Dzib+Y45FmN/HGFrHMOYPfNfYove6nWJF9Tf77uQH9\n" +
                        "n36u3Pz6DsPjTtAHMvjc7elF2HIN4KzibFJOGwYLjla4SACh4nffzaAysy5zaw7g\n" +
                        "qLNzVrgCDOYmOguFng9/QMPz85DnbbdmndLpYSvNkuBcUGMkuYDa2+hOALsgIuoT\n" +
                        "ow8nhUD/yFyCDQM7IIJS63f/kRMl/qaM65KajooBv3XWFXkNsyP+va+B1f7W6M3v\n" +
                        "b5m+/EZgML04YTiipcaetfX2/n4pHyjCUexzJOiUa7sG6pg6BkdwHi5j/j7qvswh\n" +
                        "/T3CgfVzm4iAHvYb6KoJXNQhJkoIyAonYXhZFwPBErb+gsLkiOm99/I6uvMRqBVp\n" +
                        "GHsb5Yx/E5TVd9Jwsk1igma0cJzgufftTyiefiVFn1rFSB2kSL9WPEuDOqdaslWG\n" +
                        "XPxBuHWxC/ALSSIutBfb9kV2UYo90uBfseo1mZrj3qPrdcKZXdwG5M+fOMylHQbQ\n" +
                        "2aOTob1hxfXB6U5tLoDoI8VJvC0ZBB7JPJNWnUz8+uBtDJfThdcDTuq0zOiCP3FF\n" +
                        "1LUzcb8ikL2DXNfC3qe1TEg5EWE0QMG0H59DuDxuDD73cxzqxEUapK3edibvfHmN\n" +
                        "lBVmvvzMNsDcKLVyPQ/DhQFl2xS00cH9DSlchDKPLALhkrbtIQV1xI8VuNF8hDvq\n" +
                        "L5VVHeE949GgLY2asmcrPgleXCwIBSuVWQMlSm16EPaMcnXV9vI49aqjgqrpNzzQ\n" +
                        "YGQtEgsXj06lLY6ySdnABmVGrLAywrrh/TjNdIp3yqG98BNpXqM20wR6ggMkf7XX\n" +
                        "FSly10bQ/ApCXHf1a+GSefA0NAO2uU0GQRJmo95CdV2vwfQqycdVCfePRndsCUOH\n" +
                        "uO1AGjIcUjv97INH9od4hkrN5JFhhhYVrPqLlrfUsEQAaDNg+Wdx8FrlkImlSny+\n" +
                        "dn+bH3OWqhD9i+y7hAX4Z6dCGId/Ct4Oytv0Oo48lhYZnukW1zAicgvruM4rBTmn\n" +
                        "GBUWHNX1gS8kSQ+gMit3GUmAtTPOQ4RNV8PHkPbcyOPUHIdTW6rZAuGiwhM/wwXP\n" +
                        "WcuV4awvbH3rC53FVxrgE5J/9YnkMHPPfn5WCz7I3/2VozDrNbxEPTTVo0r42nW/\n" +
                        "ZvW6POzIjrrYEjtzkdfZS7ar1vHo+LsmzzsJJ1L0nOZwprqFxg09860whikkz9+K\n" +
                        "Mt0UsHuIhWm8077kwEtv8NLrHxAqLkBxf8wVeQGCsw8eMyAQFPPa5bQneUaQck4F\n" +
                        "vwG1ekxJT43vHMk8MTzKkClhn50s6EnVAqJsRXrmEr2CQeZpuSaATCOZMB1gWfHi\n" +
                        "PXoH3PI85zoyqRuuO/pHAbsCi1hInXavnodJaKvZxtIOItAna8+6c8Jbn35GvW8E\n" +
                        "+gtxpJbhOSSB2Dt+eh49jlhUnOPOa0BJwg/uyxSzMLk4CRNszVk/QPXBh+kqH5XF\n" +
                        "qsqMqweVk1jS3dC/EMh1fKa3k0sdFxmmCy47hxw+HexUeWR8DGtiCqz6bhVTBDSB\n" +
                        "ZAphngxJ/vAdYkNAE8k/XyuPYFM4aJ0GXrBbO+Hxy3BhzRYhs0f24KmtivKw5uGr\n" +
                        "BpdX8Nm6QIS5HCd16aiVOF7YqpnmfUdo9TKGJqvN+6jj4aWgHUrkaT0Gw8x1YMHz\n" +
                        "bd5rCb+UvUPYG6FcWrK0d/8SyIyS8SoJU7CT9Fahl68JueAjPO4RqHM2/fJBI+TC\n" +
                        "/+M2O+iVm3MzcywTHxZo8hBA0/49/ldujih4ENA+3+C5PODypW3UzIhalJELQJZE\n" +
                        "itXxemGc14IX58y81uggWSxyZXqHMhKtErR4cTWclHqbXMoPMjqhD7kfCYDKP+z8\n" +
                        "f1KNXU8RMqdEaX3ZBUQ9em34pEqsnoH+rjvT2RxbhccWPK9BgUIR11kJTtnbQiBO\n" +
                        "C4YKreA8LeRHJsNFrp9fUU9YANUHxRmSJOM06eswu4Nx+cQN3y5t0Za5fzrr94RL\n" +
                        "yU5VokQVQoQZEN3y8VY4fdLhLgR3GCQMCsp9MGeB26IxAy1cgneJTf0mKar3FDaG\n" +
                        "bgZFGnQotcPOKjXBBXE1wf0Oo2H3ZxuS6wcS8MFI8qCsJqwmbeQYtb9CveiY2l1S\n" +
                        "BzecaSsvJ9io74zSxnvHvwC/wOfDSbHA8lZp/fFzwFAhjcytCHs+M2G3Wkf0LU8c\n" +
                        "kCYOioJPgY1WL9TZVphuNFHCiOu3Nkqv4XGSBaBddnPmXsisD69a7nnI4YX11Npo\n" +
                        "AYPj+eHFCTivcgZenrUYd+03VunxlWfua7ffOqalnWt89F6CingZzULZz4jltj3Y\n" +
                        "sMbLslrdxxZ3YyM9D02I6Vi39H0fHnAeWUqh3HukDIymFL7WkFTtvhKgi2152iK8\n" +
                        "MViKGGGFP+Q4dCH/Lfz7HucfBUZnDW0cSE60Z5xgGWABeNizOvVvCpiGah9RM55l\n" +
                        "xQ3NCamaHtWvMUUTSOqe9H9DQgRwijViDNCdG27qPIaKyiLYwW6bumfKJUbG3eZ9\n";
//        String publicKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("public_key.pem").toURI())));

        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;

        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet,
                        MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen
                        - offSet);/*ww w  .  ja v a2 s . c o m*/
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }
}