package main;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESencryption {
    // Set the encryption key
    public static SecretKey generateSecretKey() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] keyBytes = new byte[16];
            secureRandom.nextBytes(keyBytes);
            String base64Key = Base64.getEncoder().encodeToString(keyBytes);
            return convertToSecretKey(base64Key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKey convertToSecretKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }
 private static SecretKey convertToSecretKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "AES");
    }
 public static SecretKey convertToSecretKeyFromBytes(byte[] keyBytes) {
        return convertToSecretKey(keyBytes);
    }
   public static String convertSecretKeyToString(SecretKey secretKey) {
        byte[] encodedKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    public static SecretKey convertStringToSecretKey(String secretKeyString) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        return new javax.crypto.spec.SecretKeySpec(decodedKey, "AES");
    }
    // Encryption part
    public static String encrypt(String strToEncrypt, SecretKey secretKey) {
        try {
            // Generate a random IV (Initialization Vector)
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

            // Combine IV and encrypted data into a single byte array
            byte[] combinedBytes = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combinedBytes, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combinedBytes, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combinedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Decryption part
    public static String decrypt(String strToDecrypt, SecretKey secretKey) {
        try {
            byte[] combinedBytes = Base64.getDecoder().decode(strToDecrypt);

            // Extract the IV from the combined bytes
            byte[] iv = new byte[16];
            System.arraycopy(combinedBytes, 0, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            // Decrypt the remaining bytes after the IV
            byte[] encryptedBytes = new byte[combinedBytes.length - iv.length];
            System.arraycopy(combinedBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}