package utilities.graphics.largeAttack;

import utilities.characters.LargeAttack;
import utilities.graphics.GLargeAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of LargeAttackMagicBall and sets it.
 *
 */
public final class GLargeAttackMagicBall extends GLargeAttack {

    public GLargeAttackMagicBall(LargeAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/largeMagicBall.png")));
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
        BufferedImage sp[] = new BufferedImage[1];
        sp[0] = im;
        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
