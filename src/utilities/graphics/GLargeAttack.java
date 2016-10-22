package utilities.graphics;

import utilities.characters.LargeAttack;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import utilities.communications.Comms;

/**
 * This class only has the sprites of this large attack and the actual image
 * that is being shown.
 */
public abstract class GLargeAttack implements Graphics {

    private BufferedImage actual, image;
    private BufferedImage sprites[];
    private final LargeAttack largeAttack;
    private int frame;
    private boolean ended;

    private GraphicsGame graphics;

    private Timer timer;
    private TimerTask task;

    /**
     * The constructor of this class and the method setImages are done with the
     * intention of reducing the repetitive code.
     *
     * @param a the character that has these graphics.
     */
    public GLargeAttack(LargeAttack a) {
        largeAttack = a;
        image = null;
        actual = null;
        sprites = null;
        frame = 0;
        ended = false;
    }

    /**
     * This method is overriten by the different large attacks.
     */
    protected abstract void setImages();

    /**
     * This method shows the animation of the large attack.
     */
    @Override
    public void changeSprite() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                actual = sprites[frame];
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!ended) {
                            graphics.paintView(largeAttack.getCoordinate(), actual);
                        } else {
                            graphics.paintMap(largeAttack.getCoordinate());
                        }
                    }
                });
                ++frame;
                frame %= sprites.length;
            }
        };
        timer.scheduleAtFixedRate(task, 0, Comms.delayShowLargeAttack);

    }

    /**
     * This method ends the large attack.
     */
    public void end() {
        timer.cancel();
        ended = true;
    }

    //Getters
    /**
     * Gets actual.
     *
     * @return actual.
     */
    public BufferedImage getActual() {
        return actual;
    }

    /**
     * Gets image.
     *
     * @return image.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Gets sprites.
     *
     * @return sprites.
     */
    public BufferedImage[] getSprites() {
        return sprites;
    }

    /**
     * Gets frame.
     *
     * @return frame.
     */
    public int getFrame() {
        return frame;
    }

    /**
     * Gets graphics.
     *
     * @return graphics.
     */
    public GraphicsGame getGraphics() {
        return graphics;
    }

    /**
     * Gets ended.
     *
     * @return ended.
     */
    public boolean isEnded() {
        return ended;
    }

    //Setters
    /**
     * Sets actual.
     *
     * @param actual
     */
    public void setActual(BufferedImage actual) {
        this.actual = actual;
    }

    /**
     * Sets image.
     *
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Sets sprites and assigns sprites[0] to actual.
     *
     * @param sprites
     */
    public void setSprites(BufferedImage[] sprites) {
        this.sprites = sprites;
        actual = sprites[0];
    }

    /**
     * Sets frame.
     *
     * @param frame
     */
    public void setFrame(int frame) {
        this.frame = frame;
    }

    /**
     * Sets graphics.
     *
     * @param graphics
     */
    public void setGraphics(GraphicsGame graphics) {
        this.graphics = graphics;
    }

    /**
     * Sets ended.
     *
     * @param ended
     */
    public void setEnded(boolean ended) {
        this.ended = ended;
    }

}
