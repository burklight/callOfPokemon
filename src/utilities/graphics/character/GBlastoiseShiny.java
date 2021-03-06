package utilities.graphics.character;

import utilities.characters.OurCharacter;
import utilities.graphics.GCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with BlastoiseShiny's sprites and sets it.
 *
 */
public class GBlastoiseShiny extends GCharacter {

    public GBlastoiseShiny(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/blastoiseShiny.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
