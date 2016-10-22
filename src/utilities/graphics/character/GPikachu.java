package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Pikachu's sprites and sets it.
 *
 */
public class GPikachu extends GCharacter {

    public GPikachu(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/pikachu.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
