package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Marcel's sprites and sets it.
 *
 */
public class GMarcel extends GCharacter {

    public GMarcel(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/marcel.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
