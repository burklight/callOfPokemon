package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with SquirtleShiny's sprites and sets it.
 *
 */
public class GSquirtleShiny extends GCharacter {

    public GSquirtleShiny(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/squirtleShiny.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
