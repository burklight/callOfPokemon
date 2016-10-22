package utilities.graphics.character;

import utilities.characters.OurCharacter;
import utilities.graphics.GCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Raichu's sprites and sets it.
 *
 */
public class GRaichu extends GCharacter {

    public GRaichu(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/raichu.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
