package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;

public class Squirtle extends OurCharacter {
    
    public Squirtle(Container c, OurMap m) throws Exception{
        type=4;
        sprite = ImageIO.read(new File("characters/squirtle.png"));
        setImages(sprite);
        insert(c,m);
    }
    
}