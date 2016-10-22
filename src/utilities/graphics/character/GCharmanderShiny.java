package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with CharmanderShiny's sprites and sets it.
 *
 */
public class GCharmanderShiny extends GCharacter {

    public GCharmanderShiny(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/charmanderShiny.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
