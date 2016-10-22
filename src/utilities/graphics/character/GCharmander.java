package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Charmander's sprites and sets it.
 *
 */
public class GCharmander extends GCharacter {

    public GCharmander(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/charmander.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
