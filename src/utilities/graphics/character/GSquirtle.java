package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Squirtle's sprites and sets it.
 *
 */
public class GSquirtle extends GCharacter {

    public GSquirtle(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/squirtle.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
