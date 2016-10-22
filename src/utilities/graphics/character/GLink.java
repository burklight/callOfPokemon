package utilities.graphics.character;

import utilities.characters.OurCharacter;
import utilities.graphics.GCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Link's sprites and sets it.
 *
 */
public class GLink extends GCharacter {

    public GLink(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/link.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
