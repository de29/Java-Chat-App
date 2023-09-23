package component.client.secondaries;

import component.client.List;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
//import rojeru_san.complementos.RSPopuMenu;

public class Haut_User extends javax.swing.JPanel {

    public Haut_User() {
        initComponents();
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Supprimer Contact");
        popupMenu.add(deleteMenuItem);
        
        
    }
    public void setUserName(String userName){
        lbName.setText(userName);
    }
    public String getUserName(){
        return lbName.getText();
    }
    
    public void statusActive(){
        lbStatus.setText("En ligne");
        lbStatus.setForeground(new Color(0,138,96));
    }
    
    public void statusOffline(){
        lbStatus.setText("Hors ligne");
        lbStatus.setForeground(new Color(160,160,160));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layer = new javax.swing.JLayeredPane();
        lbName = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        layer.setLayout(new java.awt.GridLayout(0, 1));

        lbName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbName.setText("Assiya");
        layer.add(lbName);

        lbStatus.setForeground(new java.awt.Color(0, 138, 96));
        lbStatus.setText("En ligne");
        layer.add(lbStatus);

        add(layer, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 269, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 40, 40));
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        List myList = new List();
        myList.deletePeople(getUserName());
    }//GEN-LAST:event_jLabel1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane layer;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbStatus;
    // End of variables declaration//GEN-END:variables
}
