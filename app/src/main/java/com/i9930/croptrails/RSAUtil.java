package com.i9930.croptrails;

import android.annotation.SuppressLint;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {


//    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
//    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";


    private static String publicKey = "MIICCgKCAgEAt954OSnRpX4ZMuHCjEvCpSUJvi2VkKhOoBpWIApeCX7js33BDV51pM/KZX78xmF0av4ZDKnuPWzckK4E55Igx/+5Q8+zKn3WhfsJ8EXFUK3aR9nQUXVgeBbr1icWlXEThzHgRrACjRgMuRG1shyLI/EeHNtl3oqZTfodJv91Ov+rteO/D05nGFgqYDfstQnuOFbdWh9zXty9wZzYrYq3tItOYCc0WvXGstH7gDkvoUzJJU7lvhIAas1UZOr7Vs26h7JeP0mS13QrnQHaSoS7UHrJEy8wi+ufhH3cno4aaD+v3YlW7+QweKeqFwLeVWJtjnFnY1CDpT/V66Zar4A4AnzvNWRdmn63lM/luddldEFF3UVAikXbbpf41Bkgl/qyR69d4GfpO05CRBCsgLulrckFkqNVsipxNhZSO+PCVxIyPs0Uas8I6Km9h7CQzgdXqypNZpF+fk37EzFG/LfGyrOfL1DI4oDCDrpALohgjHwzQwFIqk8uMEPAhaEpczRzRWVdBdjIRvdLFm2qrXXVDRSC6/a+fqCm0M+bnVh72vREg8D0U2NV++0gC8IYFfBiXDRPI8LBYMt5W9JwCUi0VC5RArgu0qgcVoEYDs/BYUhZJl9mVk2U2wn0QWPXCl0PUbqmvN8+x/3DcNxvsr8dMskp/b+Fe7OT6tvBqSJiD68CAwEAAQ==";
    private static String privateKey = "5kQaVSoYtXTYSwWq4waP3JCR32yl9yK2pM472pI4xLUqH4yRxfvwzm2WicUhQn0e" +
            "Fl912Q7DHbt/0nHX5IFljYcYux89AoKxe8xhIRZ/TXrYqBHXItrOdiK4PngPXSSL" +
            "3HhSpefrEb+/utaCILJG29LqfR5oPz71SVIyX7vtg1prSdrcW4nGrToBgbYQ8ujV" +
            "zhxgw8Fa8wuJ2dqd3d8SFuPf5CBZjrSeZVd7/b/g/t/Zwbe6rA086qqJLS1akqlo" +
            "Sk2zYvQGz0yWladMefYU26S1gXh4XTshLoTHyCrA/JuIjTedw+TefDBtVR7bnfJP" +
            "RU8l/Px3FSN6wuie3cSeLjasvKnYudBXHWCaAH2ee4aN7pK73BcVvwBWVuNnzsyO" +
            "pSNLTRnswe2nVzoRvOSeH6F5cvF0/cOfi8FtyhASo6LJji288GAM9OSLcCgLzKSL" +
            "P/067678zOXgCkWyixhM+KpPfM+AygcdNBoBrl9brgnuZ2nxmzErqnAQgvOmt84/" +
            "uTKnGKIYncZGEvqn+Ou3sDmHshaHrWSMphkRY7KfBgP6fYYDmosAGAaLfITURvGX" +
            "ubQ4stI+lBU2dPVHFi/5vkUBpO0AbVB6PO7hdKhd9B+60tE5FSkmoSVgM9U6m0JY" +
            "yNGOvFeBlPwPUvvKsSQSW4Dzib+Y45FmN/HGFrHMOYPfNfYove6nWJF9Tf77uQH9" +
            "n36u3Pz6DsPjTtAHMvjc7elF2HIN4KzibFJOGwYLjla4SACh4nffzaAysy5zaw7g" +
            "qLNzVrgCDOYmOguFng9/QMPz85DnbbdmndLpYSvNkuBcUGMkuYDa2+hOALsgIuoT" +
            "ow8nhUD/yFyCDQM7IIJS63f/kRMl/qaM65KajooBv3XWFXkNsyP+va+B1f7W6M3v" +
            "b5m+/EZgML04YTiipcaetfX2/n4pHyjCUexzJOiUa7sG6pg6BkdwHi5j/j7qvswh" +
            "/T3CgfVzm4iAHvYb6KoJXNQhJkoIyAonYXhZFwPBErb+gsLkiOm99/I6uvMRqBVp" +
            "GHsb5Yx/E5TVd9Jwsk1igma0cJzgufftTyiefiVFn1rFSB2kSL9WPEuDOqdaslWG" +
            "XPxBuHWxC/ALSSIutBfb9kV2UYo90uBfseo1mZrj3qPrdcKZXdwG5M+fOMylHQbQ" +
            "2aOTob1hxfXB6U5tLoDoI8VJvC0ZBB7JPJNWnUz8+uBtDJfThdcDTuq0zOiCP3FF" +
            "1LUzcb8ikL2DXNfC3qe1TEg5EWE0QMG0H59DuDxuDD73cxzqxEUapK3edibvfHmN" +
            "lBVmvvzMNsDcKLVyPQ/DhQFl2xS00cH9DSlchDKPLALhkrbtIQV1xI8VuNF8hDvq" +
            "L5VVHeE949GgLY2asmcrPgleXCwIBSuVWQMlSm16EPaMcnXV9vI49aqjgqrpNzzQ" +
            "YGQtEgsXj06lLY6ySdnABmVGrLAywrrh/TjNdIp3yqG98BNpXqM20wR6ggMkf7XX" +
            "FSly10bQ/ApCXHf1a+GSefA0NAO2uU0GQRJmo95CdV2vwfQqycdVCfePRndsCUOH" +
            "uO1AGjIcUjv97INH9od4hkrN5JFhhhYVrPqLlrfUsEQAaDNg+Wdx8FrlkImlSny+" +
            "dn+bH3OWqhD9i+y7hAX4Z6dCGId/Ct4Oytv0Oo48lhYZnukW1zAicgvruM4rBTmn" +
            "GBUWHNX1gS8kSQ+gMit3GUmAtTPOQ4RNV8PHkPbcyOPUHIdTW6rZAuGiwhM/wwXP" +
            "WcuV4awvbH3rC53FVxrgE5J/9YnkMHPPfn5WCz7I3/2VozDrNbxEPTTVo0r42nW/" +
            "ZvW6POzIjrrYEjtzkdfZS7ar1vHo+LsmzzsJJ1L0nOZwprqFxg09860whikkz9+K" +
            "Mt0UsHuIhWm8077kwEtv8NLrHxAqLkBxf8wVeQGCsw8eMyAQFPPa5bQneUaQck4F" +
            "vwG1ekxJT43vHMk8MTzKkClhn50s6EnVAqJsRXrmEr2CQeZpuSaATCOZMB1gWfHi" +
            "PXoH3PI85zoyqRuuO/pHAbsCi1hInXavnodJaKvZxtIOItAna8+6c8Jbn35GvW8E" +
            "+gtxpJbhOSSB2Dt+eh49jlhUnOPOa0BJwg/uyxSzMLk4CRNszVk/QPXBh+kqH5XF" +
            "qsqMqweVk1jS3dC/EMh1fKa3k0sdFxmmCy47hxw+HexUeWR8DGtiCqz6bhVTBDSB" +
            "ZAphngxJ/vAdYkNAE8k/XyuPYFM4aJ0GXrBbO+Hxy3BhzRYhs0f24KmtivKw5uGr" +
            "BpdX8Nm6QIS5HCd16aiVOF7YqpnmfUdo9TKGJqvN+6jj4aWgHUrkaT0Gw8x1YMHz" +
            "bd5rCb+UvUPYG6FcWrK0d/8SyIyS8SoJU7CT9Fahl68JueAjPO4RqHM2/fJBI+TC" +
            "/+M2O+iVm3MzcywTHxZo8hBA0/49/ldujih4ENA+3+C5PODypW3UzIhalJELQJZE" +
            "itXxemGc14IX58y81uggWSxyZXqHMhKtErR4cTWclHqbXMoPMjqhD7kfCYDKP+z8" +
            "f1KNXU8RMqdEaX3ZBUQ9em34pEqsnoH+rjvT2RxbhccWPK9BgUIR11kJTtnbQiBO" +
            "C4YKreA8LeRHJsNFrp9fUU9YANUHxRmSJOM06eswu4Nx+cQN3y5t0Za5fzrr94RL" +
            "yU5VokQVQoQZEN3y8VY4fdLhLgR3GCQMCsp9MGeB26IxAy1cgneJTf0mKar3FDaG" +
            "bgZFGnQotcPOKjXBBXE1wf0Oo2H3ZxuS6wcS8MFI8qCsJqwmbeQYtb9CveiY2l1S" +
            "BzecaSsvJ9io74zSxnvHvwC/wOfDSbHA8lZp/fFzwFAhjcytCHs+M2G3Wkf0LU8c" +
            "kCYOioJPgY1WL9TZVphuNFHCiOu3Nkqv4XGSBaBddnPmXsisD69a7nnI4YX11Npo" +
            "AYPj+eHFCTivcgZenrUYd+03VunxlWfua7ffOqalnWt89F6CingZzULZz4jltj3Y" +
            "sMbLslrdxxZ3YyM9D02I6Vi39H0fHnAeWUqh3HukDIymFL7WkFTtvhKgi2152iK8" +
            "MViKGGGFP+Q4dCH/Lfz7HucfBUZnDW0cSE60Z5xgGWABeNizOvVvCpiGah9RM55l" +
            "xQ3NCamaHtWvMUUTSOqe9H9DQgRwijViDNCdG27qPIaKyiLYwW6bumfKJUbG3eZ9";

    @SuppressLint("NewApi")
    public static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    @SuppressLint("NewApi")
    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    @SuppressLint("NewApi")
    public static String decrypt(String data) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(privateKey));
    }

    @SuppressLint("NewApi")
    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("Dhiraj is the author"));
            System.out.println(encryptedString);
            String decryptedString = RSAUtil.decrypt(encryptedString);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }
}