package utilities.graphics.largeAttack;

import utilities.characters.LargeAttack;
import utilities.graphics.GLargeAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of LargeAttackWater and sets it.
 *
 */
public final class GLargeAttackWater extends GLargeAttack {

    public GLargeAttackWater(LargeAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/largeWater.png")));
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
        BufferedImage sp[] = new BufferedImage[7];
        for (int i = 0; i < 3; ++i) {
            sp[i] = im.getSubimage(191 * i, 0, 192, 192);
        }
        for (int i = 0; i < 4; ++i) {
            sp[3 + i] = im.getSubimage(191 * i, 192, 192, 192);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
