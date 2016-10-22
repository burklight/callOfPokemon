package utilities.graphics.largeAttack;

import utilities.characters.LargeAttack;
import utilities.graphics.GLargeAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of LargeAttackGrass and sets it.
 *
 */
public final class GLargeAttackGrass extends GLargeAttack {

    public GLargeAttackGrass(LargeAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/largeGrass.png")));
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
        sp[0] = im.getSubimage(216, 25, 142, 142);
        for (int i = 0; i < 3; ++i) {
            sp[i + 1] = im.getSubimage(191 * i + 25, 216, 142, 142);
        }
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
