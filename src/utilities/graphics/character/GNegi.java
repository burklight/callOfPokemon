package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Negi's sprites and sets it.
 *
 */
public class GNegi extends GCharacter {

    public GNegi(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/Negi.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
