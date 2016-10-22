package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with BulbasurShiny's sprites and sets it.
 *
 */
public class GBulbasurShiny extends GCharacter {

    public GBulbasurShiny(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/bulbasurShiny.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
