package component.client.secondaries;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class MenuBtn extends JButton {

    public MenuBtn() {
        setContentAreaFilled(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setBackground(new Color (242,242,242));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isSelected()){
            g.setColor(new Color(72,72,72));
            g.fillRoundRect(0,getHeight()-5,getWidth(),getHeight(),0,0);
        }
    }
    
    
}
