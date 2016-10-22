package utilities.graphics.largeAttack;

import utilities.characters.LargeAttack;
import utilities.graphics.GLargeAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of LargeAttackPlasmaBall and sets it.
 *
 */
public final class GLargeAttackPlasmaBall extends GLargeAttack {

    public GLargeAttackPlasmaBall(LargeAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/largePlasmaBall.png")));
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

        for (int i = 0; i < 3; ++i) {
            sp[i] = im.getSubimage(200 * (i + 1) + 10, 0, 150, 150);
        }
        sp[3] = im.getSubimage(200 * 2 + 10, 0, 150, 150);

        setSprites(sp);
        setActual(getSprites()[0]);
    }

}
