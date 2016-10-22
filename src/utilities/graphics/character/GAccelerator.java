package utilities.graphics.character;

import utilities.graphics.GCharacter;
import utilities.characters.OurCharacter;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture with Accelerator's sprites and sets it.
 *
 */
public class GAccelerator extends GCharacter {

    public GAccelerator(OurCharacter c) {
        super(c);
        try {
            setImage(ImageIO.read(new File("characters/Accelerator.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
