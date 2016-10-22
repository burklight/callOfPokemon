package utilities.graphics.shortAttack;

import utilities.characters.ShortAttack;
import utilities.graphics.GShortAttack;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * This class reads the picture of ShortAttackSOS and sets it.
 *
 */
public final class GShortAttackSOS extends GShortAttack {

    public GShortAttackSOS(ShortAttack a) {
        super(a);
        try {
            setImage(ImageIO.read(new File("attacks/shortSOS.png")));
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
        BufferedImage sp[] = new BufferedImage[10];
        for (int i = 0; i < 5; ++i) {
            sp[i] = im.getSubimage(191 * i, 0, 192, 192);
        }
        for (int i = 0; i < 5; ++i) {
            sp[i + 5] = im.getSubimage(191 * i, 192, 192, 192);
        }
        setSprites(sp);
    }

}
