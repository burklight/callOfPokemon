package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Haruhi's sprites and sets it.
 *
 */
public class GHaruhi extends GCharacter {

    public GHaruhi(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/Haruhi.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
