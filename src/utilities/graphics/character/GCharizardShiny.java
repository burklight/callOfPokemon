package utilities.graphics.character;

import utilities.characters.OurCharacter;
import utilities.graphics.GCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with CharizardShiny's sprites and sets it.
 *
 */
public class GCharizardShiny extends GCharacter {

    public GCharizardShiny(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/charizardShiny.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
