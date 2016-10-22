package utilities.graphics;

import utilities.characters.OurCharacter;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import utilities.communications.Comms;

public abstract class GCharacter implements Graphics {

    /**
     * This class only has the sprites of this character and the actual image
     * that is beingn showed.
     */
    private volatile BufferedImage actual, image;
    private BufferedImage imageUp[], imageDown[], imageLeft[], imageRight[], sprites[];
    private OurCharacter character;
    int frame;
    private boolean ended;

    private GraphicsGame graphics;

    private Timer timer;
    private TimerTask task;

    /**
     * The constructor of this class and the method setImages are done with the
     * intention of reducing the repetitive code.
     *
     * @param c the character that has these graphics.
     */
    public GCharacter(OurCharacter c) {
        character = c;
        frame = 0;
        image = null;
        actual = null;
        ended = false;
        sprites = new BufferedImage[4];
        imageDown = new BufferedImage[4];
        imageUp = new BufferedImage[4];
        imageLeft = new BufferedImage[4];
        imageRight = new BufferedImage[4];
    }

    /**
     * Gets the subimages and assigns them to the correct array.
     */
    protected void setImages() {
        for (int i = 0; i < 4; i++) {
            imageDown[i] = image.getSubimage(63 * i + 5, 10, 53, 53);
            imageLeft[i] = image.getSubimage(63 * i + 5, 74, 53, 53);
            imageRight[i] = image.getSubimage(63 * i + 5, 138, 53, 53);
            imageUp[i] = image.getSubimage(63 * i + 5, 202, 53, 53);
        }
        actual = imageDown[0];
    }

    /**
     * This method shows the animation of the pokemon.
     */
    @Override
    public void changeSprite() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                switch (character.getDirection()) {
                    case Comms.up:
                        sprites = imageUp;
                        break;
                    case Comms.down:
                        sprites = imageDown;
                        break;
                    case Comms.left:
                        sprites = imageLeft;
                        break;
                    case Comms.right:
                        sprites = imageRight;
                        break;
                }
                ++frame;
                frame %= sprites.length;
                actual = sprites[frame];
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!ended) {
                            graphics.paintView(character.getCoordinate(), actual);
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, Comms.delayCharacterAnimation);
    }

    /**
     * Method to die. Sets ended to true.
     *
     * @param c
     */
    public void die(Coordinate c) {
        graphics.paintView(c, graphics.getMap().getObs()[Comms.numObstacles]);
        ended = true;
        timer.cancel();
    }

    //Getters
    /**
     * Gets actual.
     *
     * @return actual
     */
    public BufferedImage getActual() {
        return actual;
    }

    /**
     * Gets image.
     *
     * @return image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Gets imageUp.
     *
     * @return imageUp
     */
    public BufferedImage[] getImageUp() {
        return imageUp;
    }

    /**
     * Gets imageDown.
     *
     * @return imageDown
     */
    public BufferedImage[] getImageDown() {
        return imageDown;
    }

    /**
     * Gets imageLeft.
     *
     * @return imageLeft
     */
    public BufferedImage[] getImageLeft() {
        return imageLeft;
    }

    /**
     * Gets imageRight.
     *
     * @return imageRight
     */
    public BufferedImage[] getImageRight() {
        return imageRight;
    }

    /**
     * Gets character.
     *
     * @return character
     */
    public OurCharacter getCharacter() {
        return character;
    }

    /**
     * Gets ended.
     *
     * @return ended
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
     * Sets imageUp.
     *
     * @param imageUp
     */
    public void setImageUp(BufferedImage[] imageUp) {
        this.imageUp = imageUp;
    }

    /**
     * Sets imageDown.
     *
     * @param imageDown
     */
    public void setImageDown(BufferedImage[] imageDown) {
        this.imageDown = imageDown;
    }

    /**
     * Sets imageLeft.
     *
     * @param imageLeft
     */
    public void setImageLeft(BufferedImage[] imageLeft) {
        this.imageLeft = imageLeft;
    }

    /**
     * Sets imageRight.
     *
     * @param imageRight
     */
    public void setImageRight(BufferedImage[] imageRight) {
        this.imageRight = imageRight;
    }

    /**
     * Sets character.
     *
     * @param character
     */
    public void setCharacter(OurCharacter character) {
        this.character = character;
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
