package server;
import server.CertificateLoader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.sql.*;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import static server.Profile.isOnline;

class GestionClient extends Thread{
	private final DataInputStream dis;
	private final DataOutputStream dos;
	private final Socket commthread;
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private Profile profile=null;
	private String msg;
	private String receiver;
        private SecretKey clientPublicKey;
	private boolean loginOK=false;
	public GestionClient(Socket s, DataInputStream diss, DataOutputStream doss , SecretKey clientpublickey){
		this.commthread = s;
		this.dis = diss;
		this.dos = doss;
                this.clientPublicKey = clientpublickey;
		try{
                        
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/wechat","root","");
			this.stmt = this.conn.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Erreur database");
			error();
		}
		if(this.conn != null && this.stmt != null){
			this.start();
		}
		else{
			error();
		}
	}
	@Override
	public void run(){
		while (true){
			while(!this.loginOK){
				try{
					String message = this.dis.readUTF();
					String strarray[] = message.split(" ",2);
					if(strarray.length==2){
						if(strarray[0].equals("login")){
							this.loginOK=login(strarray[1].toLowerCase(),dis.readUTF());
							if(this.loginOK){//load Message from DB
								try{
									String sql="select * from messages where receiver_Email='"+this.profile.getEmail()+"'";
									rs=stmt.executeQuery(sql);
									while(rs.next()){
                                        if(rs.getString("messageType").equals("text"))
                                            dos.writeUTF(rs.getString("messageType")+"@@@"+rs.getString("sender_Email")+"@@@"+rs.getString("date")+"@@@"+rs.getString("message"));
                                        else{
                                            dos.writeUTF(rs.getString("messageType")+"@@@"+rs.getString("sender_Email")+"@@@"+rs.getString("date")+"@@@"+rs.getString("fileName"));
                                            byte[] bytes=Base64.getDecoder().decode(rs.getString("message"));
                                            this.dos.writeInt(bytes.length);
                                            this.dos.write(bytes);
                                        }
                                    }
                                     sql="delete from messages where receiver_Email='"+this.profile.getEmail()+"'";
									stmt.executeUpdate(sql);
									ArrayList<Integer> Group_messages_received=new ArrayList<Integer>();
									sql="select * from Groupe_Messages join Groupe_Message_content on Groupe_Messages.GMC_id=Groupe_Message_content.GMC_id where receiver_id='"+this.profile.getId()+"'";
									rs=stmt.executeQuery(sql);
									while(rs.next()){
										Group_messages_received.add(rs.getInt("GMC_id"));
                                        if(rs.getString("messageType").equals("text"))
                                             dos.writeUTF(rs.getString("messageType")+"@@@Group@@@"+rs.getString("Groupe_id")+"@@@"+rs.getString("sender_Email")+"@@@"+rs.getString("date")+"@@@"+rs.getString("content"));
                                        else{
                                            dos.writeUTF(rs.getString("messageType")+"@@@Group@@@"+rs.getString("Groupe_id")+"@@@"+rs.getString("sender_Email")+"@@@"+rs.getString("date")+"@@@"+rs.getString("fileName"));
                                            byte[] bytes=Base64.getDecoder().decode(rs.getString("content"));
                                            this.dos.writeInt(bytes.length);
                                            this.dos.write(bytes);
                                        }
									}
									sql="delete from Groupe_Messages where receiver_id='"+this.profile.getId()+"'";
									stmt.executeUpdate(sql);
									this.profile.unlockMe();
									for(int i=0;i<Group_messages_received.size();i++){
										sql ="select * from Groupe_Messages where GMC_id='"+Group_messages_received.get(i)+"'";
										rs=stmt.executeQuery(sql);
										if(!rs.next()){
											sql="delete from Groupe_Message_content where GMC_id='"+Group_messages_received.get(i)+"'";
											stmt.executeUpdate(sql);
										}
									}
									this.profile.connected(this.dos);
								}
								catch(Exception e){
									e.printStackTrace();
									error();
									break;
								}
							}
						}
						else if(strarray[0].equals("register")){
							if(register(strarray[1],dis.readUTF(),dis.readUTF()))
								dos.writeUTF("registerOK");
							else
								dos.writeUTF("registerError");
						}
						else
							System.out.println("Unknown command : "+message);
					}
					else
						System.out.println("Unknown command : "+message);
				}
				catch(Exception e){
					System.out.println("Client disconnected");
					error();
					break;
				}
			}
			if(!this.loginOK)
				error();
			try {
				//User Connect, we wait for what he wanna do
                                Certificate certificate =  CertificateLoader.loadCertificate();
				receiver = dis.readUTF();
				System.out.println(this.profile.getName()+" :  "+receiver.substring(0, Math.min(50, receiver.length())));
                                String strarray[] = receiver.split("@@@",2);
				if(strarray.length==2){ //confirme que le split a marché
                                    switch (strarray[0]) {
                                        case "messageUser" -> {
                                            System.out.println("receiver : " + strarray[1]);
                                            msg = dis.readUTF();
                                            String[] message = msg.split("@@@",4);
                                            String macserver = dis.readUTF();
                                            System.out.println("mac server : "+macserver);
                                            String derivedkey = dis.readUTF();
                                            System.out.println("derived key : "+derivedkey);
                                            String k = HMAC.deriveSymmetricKey(AESencryption.convertSecretKeyToString(Server.getServerkey()),AESencryption.convertSecretKeyToString(this.profile.getkey()));
                                            System.out.println("calculated key : "+k);
                                         
                                            
                                            boolean isValid = false;
                                        try {
                                            isValid = HMAC.verifyHMAC(message[3], k, macserver, certificate);
                                            System.out.println(isValid);
                                        } catch (NoSuchAlgorithmException ex) {
                                            Logger.getLogger(GestionClient.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (InvalidKeyException ex) {
                                            Logger.getLogger(GestionClient.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                            
                                            String result=message[0];
                                            message[3]= AESencryption.decrypt( message[3], Server.getServerkey()); 
                                            Profile d = null;
                                               for (Map.Entry<Profile, SecretKey> entry : Profile.getkeys().entrySet()) {
                                                       Profile p = entry.getKey();
                                                       SecretKey secretKey = entry.getValue();
                                                       if(p.getEmail().equals( strarray[1])){
                                                           d = p;
                                                       }
                                                    }
                                          
                                            if(d!=null){
                                                  message[3]= AESencryption.encrypt( message[3], Profile.getkeys().get(d));
                                                  System.out.println("**************** crypted client key*****************");
                                            }else {
                                                  message[3]= AESencryption.encrypt( message[3], Server.getServerkey());
                                                  System.out.println("**************** crypted server key*****************");
                                            }
                                            System.out.println("encrypted message :"+ message[3]);
                                              for(int i=1; i< message.length;i++){
                                                result = result + "@@@"+message[i];
                                              }
                                               System.out.println(" result :"+ result);
                                               if(isValid==true){
                                                    this.profile.sendMessage(strarray[1], result);
                                               }else {
                                                     System.out.println("***********************INVALID  HMAC*******************************");
                                               }
                                           
                                        }
                                        case "messageGroup" -> {
                                            System.out.println("receiver : Groupe_id = " + strarray[1]);
                                            msg=dis.readUTF();
                                            if(this.profile.sendMessageToGroup(Integer.parseInt(strarray[1]), msg))
                                                System.out.println(this.profile.getName()+" in "+strarray[1]+" : "+ msg);
                                            else
                                                System.out.println("User "+this.profile.getName()+" is not in group "+strarray[1]);
                                        }
                                        case "checkIfExist" -> {
                                            String sql="select * from Users where email='"+strarray[1]+"'";
                                            try {
                                                rs=stmt.executeQuery(sql);
                                                if(rs.next())
                                                    dos.writeUTF("exist@@@"+strarray[1]);
                                                else
                                                    dos.writeUTF("notExist@@@"+strarray[1]);
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        case "call" -> {
                                            System.out.println(receiver);
                                            Profile p = isOnline(strarray[1]);
                                            if (p != null) {
                                                try {
                                                    Profile.getMap().get(p).writeUTF("call@@@"+this.profile.getEmail());
                                                    System.out.println("sent call");
                                                }catch(Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        case "answer" -> {
                                            System.out.println(receiver);
                                            Profile p = isOnline(strarray[1]);
                                            if (p != null) {
                                                try {
                                                    Profile.getMap().get(p).writeUTF("answer@@@"+this.profile.getEmail()+"@@@"+strarray[2]);
                                                    System.out.println("answer");
                                                }catch(Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        default -> System.out.println("1 receiver not found " + strarray[0]+ " " + strarray[1]);
                                    }
				}
				else if(receiver.equals("Disconnect")){
					this.loginOK=false;
					this.profile.disconnect();
                                        System.out.println("here 1");
					System.out.println(this.profile.getName()+" disconnected");
                                        
				}
				else
					System.out.println("2 receiver not found " + receiver);
			}
			catch (IOException e) {
				if(this.profile!=null)  
                                        System.out.println("here 2");
                                File directory = new File("C:\\Users\\hp\\Downloads\\Projet Reseau\\Projet Reseau\\WeChat\\Conversations\\");
                                File[] files = directory.listFiles(new FilenameFilter() {
                                public boolean accept(File directory, String fileName) {
                                         return fileName.startsWith(profile.getEmail());
                                }
                                });

                               if (files.length > 0) {
                                   for (File fileToEmpty : files) {
                                          try {
                                     PrintWriter writer = new PrintWriter(fileToEmpty);
            writer.print("");
            writer.close();
            System.out.println("File contents deleted successfully: " + fileToEmpty.getName());
                                       } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + fileToEmpty.getName());
        }
    }
} else {
    System.out.println("No files found starting with "+profile.getEmail());
}
					System.out.println(profile.getEmail()+" disconnected");
				error();
				break;
			} catch (Exception ex) {
                        Logger.getLogger(GestionClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
	}
	private  boolean login(String login, String password){
		try{
                     
			String sql = "SELECT * FROM users WHERE email = '"+login+"' AND password = '"+password+"'";
			this.rs = this.stmt.executeQuery(sql);
			if(this.rs.next()){
                if(rs.getString("password").equals(password)){
                    this.dos.writeUTF("Login Successful");
                    System.out.println("here 0");
                    System.out.println(login+" : Connexion reussite");
                    this.profile = new Profile(this.rs.getInt("user_id"),this.rs.getString("email"),
                    this.rs.getString("name"), this.dos, this.dis, this.stmt,this.clientPublicKey);
                    this.profile.lockMe();
                    return true;
                }
			}
			this.dos.writeUTF("Login Failed");
			System.out.println(login+" : Connection echouee");
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	private boolean register(String name, String Email, String password){
		try{
			String sql = "SELECT * FROM users WHERE email = '"+Email+"'";
			this.rs = this.stmt.executeQuery(sql);
			if(this.rs.next()){
				this.dos.writeUTF("Email already used");
				System.out.println(Email+" existe dejà !");
				return false;
			}
			else{   
                            ///////////////////////////////
				sql = "INSERT INTO users (name, email, password) VALUES ('"+name+"','"+Email+"','"+password+"')";
				this.stmt.executeUpdate(sql);
				this.dos.writeUTF("Registration Successful");
				System.out.println("Inscription reussite");
				return true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	private  void error(){
		if(this.loginOK&&this.profile!=null){
			this.profile.disconnect();
		}
		try{
			this.dis.close();
			this.dos.close();			
		}catch(IOException e3){
			e3.printStackTrace();
		}
		try{
			this.commthread.close();
		}catch(IOException e4){
			e4.printStackTrace();
		}
	}
}