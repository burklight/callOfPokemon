package utilities.graphics.character;

import utilities.characters.OurCharacter;
import utilities.graphics.GCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with VenusaurShiny's sprites and sets it.
 *
 */
public class GVenusaurShiny extends GCharacter {

    public GVenusaurShiny(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/venusaurShiny.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
