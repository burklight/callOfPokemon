package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;

public class SquirtleShini extends OurCharacter {
    
    public SquirtleShini(Container c, OurMap m) throws Exception{
        type=4;
        sprite = ImageIO.read(new File("characters/squirtleShini.png"));
        setImages(sprite);
        insert(c,m);
    }
    
}