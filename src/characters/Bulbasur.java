package characters;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import map.OurMap;
import specifics.AttackBulbasur;

public class Bulbasur extends OurCharacter{

    public Bulbasur(Container c,OurMap m) throws Exception{
        type=5;
        attack = new AttackBulbasur();
        sprite = ImageIO.read(new File("characters/bulbasur.png"));
        setImages(sprite);
        insert(c,m);
    }

}
