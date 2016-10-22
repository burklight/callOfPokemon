package utilities.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * This class has the methods to treat images.
 */
public class TreatImage {

    /**
     * This method combines the two images passed as a parameter (player and
     * obstacle) and return this combination (combination)
     *
     * @param player
     * @param obstacle
     * @return combinedImage
     */
    public static BufferedImage combine(BufferedImage player, BufferedImage obstacle) {
        BufferedImage combinedImage = new BufferedImage(player.getWidth() - 1,
                player.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage destiny = new BufferedImage(player.getWidth(),
                player.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gg = destiny.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double) player.getWidth() / obstacle.getWidth(),
                (double) player.getHeight() / obstacle.getHeight());
        gg.drawRenderedImage(obstacle, at);
        Graphics g = combinedImage.getGraphics();
        g.drawImage(destiny, -1, 0, null);
        g.drawImage(player, -1, 0, null);
        return combinedImage;
    }

    /**
     * Paints a given image all black giving his siluete.
     *
     * @param image
     * @return siluete
     */
    public static BufferedImage setSiluete(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = 0;
                pixels[1] = 0;
                pixels[2] = 0;
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }

}
