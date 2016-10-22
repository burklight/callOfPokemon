package utilities.graphics.shortAttack;

import utilities.characters.ShortAttack;
import utilities.graphics.GShortAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of ShortAttackWater and sets it.
 *
 */
public final class GShortAttackWater extends GShortAttack {

    public GShortAttackWater(ShortAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/shortWater.png")));
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
        BufferedImage sp[] = new BufferedImage[5];
        for (int i = 0; i < 5; ++i) {
            sp[i] = im.getSubimage(191 * (4 - i), 192, 192, 192);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
