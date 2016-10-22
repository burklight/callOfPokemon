package utilities.graphics.largeAttack;

import utilities.characters.LargeAttack;
import utilities.graphics.GLargeAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of LargeAttackShadow and sets it.
 *
 */
public final class GLargeAttackShadow extends GLargeAttack {

    public GLargeAttackShadow(LargeAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/largeShadow.png")));
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
        BufferedImage sp[] = new BufferedImage[8];
        for (int i = 0; i < 3; ++i) {
            sp[i] = im.getSubimage(191 * (i + 2), 192, 192, 192);
        }
        for (int i = 0; i < 5; ++i) {
            sp[3 + i] = im.getSubimage(191 * i, 192 * 2, 192, 192);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
