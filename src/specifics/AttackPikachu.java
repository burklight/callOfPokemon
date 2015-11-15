package specifics;

import graphics.Coordinate;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import map.OurMap;

public class AttackPikachu extends Attack{
        
    public AttackPikachu(Container c, OurMap m){
        type = 3;
        setLargeImage();
        insert(c, m);
    }
    
    private void setLargeImage(){
        try {
            BufferedImage image = ImageIO.read(new File("attacks/largeElectric.png"));
            largeLen = 5;
            largeAttack = new BufferedImage[largeLen];
            for(int i=0; i<largeLen; ++i){
                largeAttack[i] = image.getSubimage(191*i, 0, 191, 191);
            }
        } catch (Exception ex) { }
    }

    @Override
    public void shortRangeAttack(int d, Coordinate c) {
        
    }
    
}
