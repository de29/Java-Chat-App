
package main;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

public class CertificateLoader {
    private static String keystorePath="C:\\xampp\\htdocs\\Projet Reseau updated\\Projet Reseau (4)\\Projet Reseau\\keystore.jks";
    private static String keystorePassword="password";
    private static String alias="myalias";
    private static String certificatePassword="password";
    public static Certificate loadCertificate() throws Exception {
        // Load the keystore
        KeyStore keystore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(keystorePath);
        keystore.load(fis, keystorePassword.toCharArray());

        // Get the certificate from the keystore
        Certificate certificate = keystore.getCertificate(alias);
        
        // Optional: Set the certificate password if required
        KeyStore.PasswordProtection protection = new KeyStore.PasswordProtection(certificatePassword.toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(alias, protection);
        
        // Return the certificate
        return certificate;
    }
}

