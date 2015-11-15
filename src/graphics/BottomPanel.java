package graphics;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel{
    
    protected Image image;
 
    public BottomPanel() {

    }
 
    public void setImage(Image newImage) {
        image = newImage;
        repaint();
    }
    
    public Image getImage(){
        return image;
    }
}
    
    

