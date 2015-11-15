package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;
import specifics.AttackPikachu;

public class Pikachu extends OurCharacter {
    
    public Pikachu(Container c, OurMap m) throws Exception{
        type=3;
        sprite = ImageIO.read(new File("characters/pikachu.png"));
        setImages(sprite);
        insert(c,m);
        attack = new AttackPikachu(c,m);
    }
    
}