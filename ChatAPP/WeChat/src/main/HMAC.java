/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMAC {

    private static final String KEY_DERIVATION_ALGORITHM = "SHA-256";

    public static String deriveSymmetricKey(String key1, String key2) throws NoSuchAlgorithmException {
        byte[] concatenatedKeys = (key1 + key2).getBytes();

        MessageDigest digest = MessageDigest.getInstance(KEY_DERIVATION_ALGORITHM);
        byte[] derivedKeyBytes = digest.digest(concatenatedKeys);
        return bytesToHex(derivedKeyBytes);
    }

    public static String calculateHMAC(String message, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        String HMAC_ALGORITHM = "HmacSHA256";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(message.getBytes());
        return bytesToHex(hmacBytes);
    }

     public static boolean verifyHMAC(String message, String key, String receivedHMAC,Certificate certificat)
            throws NoSuchAlgorithmException, InvalidKeyException {
    try {
        // Add this line to register the Bouncy Castle security provider
         Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

         Certificate certificate = certificat;

         PublicKey publicKey = certificate.getPublicKey();
         Signature signature = Signature.getInstance("SHA256withRSA", "BC"); // Specify RSA algorithm and provider
         signature.initVerify(publicKey);
         signature.update(message.getBytes());
         byte[] receivedHMACBytes = hexToBytes(receivedHMAC);
         return signature.verify(receivedHMACBytes);
     } catch (Exception e) {
         e.printStackTrace();
         return false;
     }
}

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}