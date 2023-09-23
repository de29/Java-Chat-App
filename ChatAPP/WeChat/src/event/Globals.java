package event;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import main.AESencryption;

public class Globals {

    /**
     * @return the myEmail
     */
    public static String getMyEmail() {
        return myEmail;
    }

    /**
     * @param aMyEmail the myEmail to set
     */
    public static void setMyEmail(String aMyEmail) {
        myEmail = aMyEmail.toLowerCase();
    }
    
    public static Globals instance;
    public static Socket socket;
    public static InetAddress ip;
    public static DataInputStream dis;
    public static DataOutputStream dos;
    public static Lock myWriteLock;
    private static String myEmail;
    private static SecretKey clientpublickey;
    private static SecretKey serverPublicKey;
    
    public static void initGlobals(){
        try{
            ip = InetAddress.getByName("localhost");
            socket = new Socket(ip, 5059);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            myWriteLock = new ReentrantLock();
            
            AESencryption aes=new AESencryption();
         try{
            clientpublickey = aes.generateSecretKey();
             System.out.println("client public key " + clientpublickey);
         }catch(Exception e){
            e.printStackTrace();
        }
         
            byte[] clientPublicKeyBytes = clientpublickey.getEncoded();
            dos.writeInt(clientPublicKeyBytes.length);
            dos.write(clientPublicKeyBytes);
            // Receive server's public key
            byte[] serverPublicKeyBytes = new byte[dis.readInt()];
            dis.readFully(serverPublicKeyBytes);
            serverPublicKey = aes.convertToSecretKeyFromBytes(serverPublicKeyBytes);
            System.out.println("sever public key " + serverPublicKey);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }
    public static SecretKey getServerkey(){
    
       return serverPublicKey;
    }
     public static SecretKey getClientkey(){
    
       return clientpublickey;
    }
    private Globals() {

    }
}
