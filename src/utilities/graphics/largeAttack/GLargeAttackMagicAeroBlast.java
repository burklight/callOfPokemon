package utilities.graphics.largeAttack;

import utilities.characters.LargeAttack;
import utilities.graphics.GLargeAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of LargeAttackMagicAeroBlast and sets it.
 *
 */
public final class GLargeAttackMagicAeroBlast extends GLargeAttack {

    public GLargeAttackMagicAeroBlast(LargeAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/largeAeroblast.png")));
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
        for (int i = 0; i < 3; ++i) {
            sp[i] = im.getSubimage(191 * (i + 2), 0, 192, 192);
        }
        for (int i = 0; i < 2; ++i) {
            sp[i + 3] = im.getSubimage(191 * i, 0, 192, 192);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
