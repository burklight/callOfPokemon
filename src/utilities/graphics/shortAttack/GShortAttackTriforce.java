package utilities.graphics.shortAttack;

import utilities.characters.ShortAttack;
import utilities.graphics.GShortAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of ShortAttackTriforce and sets it.
 *
 */
public final class GShortAttackTriforce extends GShortAttack {

    public GShortAttackTriforce(ShortAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/shortTriforce.png")));
            setImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the subimages of the picture previusly read and then sets them in
     * order.
     */
    @Override
    protected void setImages() {
        BufferedImage im = getImage();
        BufferedImage sp[] = new BufferedImage[4];
        for (int i = 0; i < 4; ++i) {
            sp[i] = im.getSubimage(191 * i, 0, 192, 192);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
