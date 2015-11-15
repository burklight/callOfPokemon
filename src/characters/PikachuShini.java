package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;

public class PikachuShini extends OurCharacter {
    
    public PikachuShini(Container c, OurMap m) throws Exception{
        type=3;
        sprite = ImageIO.read(new File("characters/pikachuShini.png"));
        setImages(sprite);
        insert(c,m);
    }
    
}