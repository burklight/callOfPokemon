package utilities.graphics.shortAttack;

import utilities.characters.ShortAttack;
import utilities.graphics.GShortAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of ShortAttackGeass and sets it.
 *
 */
public final class GShortAttackGeass extends GShortAttack {

    public GShortAttackGeass(ShortAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/shortGeass.png")));
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
            sp[i] = im.getSubimage(191 * i, 192, 192, 192);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
