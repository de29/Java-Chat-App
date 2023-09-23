package component.client;

import component.client.secondaries.Lte7t;
import component.client.secondaries.Body;
import component.client.secondaries.Haut_User;
import event.EventChat;
import event.PublicEvent;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;

public class Chat extends javax.swing.JPanel {
    public Lte7t CBottom =new Lte7t();
    public Body CBody =new Body();
    public Haut_User CTitle =new Haut_User();
    
    public Chat() {
        initComponents();
        PublicEvent.getInstance().addEventChat(new EventChat() {
            @Override
            public void sendMessage(String time, String text){
                CBody.addItemRight(time, text);
            }
            @Override
            public void sendAudio(String time, String path){
                CBody.addAudioRight(time, path);
            }
            @Override
            public void sendImage(String time, ImageIcon image){
                CBody.addItemRight(time, "", image);
            }

            @Override
            public void sendFile(String time, String path) {
                CBody.addFileRight(time, path);
            }
            
        });
        init();
    }
    private void init(){
        setLayout(new MigLayout("fillx, filly","0[fill,100%]0","[fill,55::10%]0[fill,78%, bottom]0[fill,shrink 0]0"));
        this.add(this.CTitle,"wrap");
        this.add(this.CBody,"wrap");
        this.add(this.CBottom,"h ::30%");
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(950, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 807, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 556, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
