package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;

public class Charmander extends OurCharacter {
    
    public Charmander(Container c,OurMap m) throws Exception{
        type=2;
        sprite = ImageIO.read(new File("characters/charmander.png"));
        setImages(sprite);
        insert(c,m);
    }
    
}
