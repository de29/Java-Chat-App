package slider;

import java.awt.Color;
import javax.swing.JSlider;

public class JsliderCustom extends JSlider {

    public JsliderCustom() {
        setOpaque(false);
        setBackground(new Color(149,189,255));
        setForeground(new Color(69, 124, 235));
        setUI(new JSliderUI(this));
    }
}
