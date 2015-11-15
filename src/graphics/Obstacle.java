package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Obstacle {
    
    private int obstacle;
    private BottomPanel bp;
    private BufferedImage bf;

    public Obstacle(int o, BottomPanel panel) {
        try {
            obstacle = o;
            bp = panel;
            bf=ImageIO.read(new File("maps/obstacle"+obstacle+".png"));
        } catch (Exception ex) {}
    }

    public int getObs() {
        return obstacle;
    }

    public BottomPanel getBp() {
        return bp;
    }

    public void setObs(int o) {
        obstacle = o;
    }

    public void setBp(BottomPanel panel) {
        bp = panel;
    }
    
    public BufferedImage getBf() {
        return bf;
    }
    
}
