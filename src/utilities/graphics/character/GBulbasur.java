package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Bulbasur's sprites and sets it.
 *
 */
public class GBulbasur extends GCharacter {

    public GBulbasur(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/bulbasur.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
