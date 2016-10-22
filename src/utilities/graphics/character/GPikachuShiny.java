package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with PikachuShiny's sprites and sets it.
 *
 */
public class GPikachuShiny extends GCharacter {

    public GPikachuShiny(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/pikachuShiny.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
