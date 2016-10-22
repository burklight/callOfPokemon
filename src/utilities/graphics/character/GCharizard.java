package utilities.graphics.character;

import utilities.characters.OurCharacter;
import utilities.graphics.GCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Charizard's sprites and sets it.
 *
 */
public class GCharizard extends GCharacter {

    public GCharizard(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/charizard.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
