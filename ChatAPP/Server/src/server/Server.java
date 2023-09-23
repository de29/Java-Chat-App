package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class Server{
    private static SecretKey serverPublicKey;
    
     public static SecretKey getServerkey(){
    
       return serverPublicKey;
    }
	public static void main(String[] args) {
        ServerSocket connSocket=null;
        try{
            connSocket = new ServerSocket(5059);
        }
        catch (Exception e2){
            System.out.println("Erreur de connexion");
        }
        
            AESencryption aes=new AESencryption();
        try {
            serverPublicKey = aes.generateSecretKey();
            System.out.println("server public key  : "+serverPublicKey);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
            Profile.inintaliseProfiles();
            while (true) {
                Socket commSocket = null;
                try {
                    commSocket = connSocket.accept();
                    System.out.println("Nouvelle connection : " + commSocket);
                    DataInputStream dis = new DataInputStream(commSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(commSocket.getOutputStream());
                    //read client public key
                      byte[] clientPublicKeyBytes = new byte[dis.readInt()];
                      dis.readFully(clientPublicKeyBytes);
                      SecretKey clientPublicKey = AESencryption.convertToSecretKeyFromBytes(clientPublicKeyBytes);
                      System.out.println("client public key"+ clientPublicKey);
                    //send server public key to client
                    byte[] serverPublicKeyBytes = serverPublicKey.getEncoded();
                    dos.writeInt(serverPublicKeyBytes.length);
                    dos.write(serverPublicKeyBytes);
                    Thread t = new GestionClient(commSocket, dis, dos, clientPublicKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
	}
}
