package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;

public class CharmanderShini extends OurCharacter {
    
    public CharmanderShini(Container c, OurMap m) throws Exception{
        type=2;
        sprite = ImageIO.read(new File("characters/charmanderShini.png"));
        setImages(sprite);
        insert(c,m);
    }
    
}