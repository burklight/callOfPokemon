package utilities.graphics.largeAttack;

import utilities.characters.LargeAttack;
import utilities.graphics.GLargeAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of LargeAttackBoomerang and sets it.
 *
 */
public final class GLargeAttackBoomerang extends GLargeAttack {

    public GLargeAttackBoomerang(LargeAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/largeBoomerang.png")));
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
            sp[i] = im.getSubimage(141 * i + 5, 4, 131, 131);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }
}
