package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;

public class BulbasurShini extends OurCharacter {
    
    public BulbasurShini(Container c, OurMap m) throws Exception{
        type=5;
        sprite = ImageIO.read(new File("characters/bulbasurShini.png"));
        setImages(sprite);
        insert(c,m);
    }
    
}