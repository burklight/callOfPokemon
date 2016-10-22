package utilities.graphics.character;

import utilities.characters.OurCharacter;
import utilities.graphics.GCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Venusaur's sprites and sets it.
 *
 */
public class GVenusaur extends GCharacter {

    public GVenusaur(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/venusaur.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
