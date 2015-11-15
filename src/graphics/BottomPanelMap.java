package graphics;

import java.awt.Graphics;

public class BottomPanelMap extends BottomPanel{
    
    @Override
    public void paint(Graphics g) {
        if (null != image) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
        } else {
            setOpaque(true);
        }
        super.paint(g);
    }
    
}
