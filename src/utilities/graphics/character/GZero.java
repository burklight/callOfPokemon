package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Zero's sprites and sets it.
 *
 */
public class GZero extends GCharacter {

    public GZero(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/zero.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
