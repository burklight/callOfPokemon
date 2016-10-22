package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Nanoha's sprites and sets it.
 *
 */
public class GNanoha extends GCharacter {

    public GNanoha(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/Nanoha.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
