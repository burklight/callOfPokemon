package utilities.image;

import javax.swing.*;
import java.awt.*;

/**
 * Class that has de BottomPanel.
 */
public class BottomPanel extends JPanel {

    protected Image image;

    public BottomPanel() {

    }

    /**
     * Sets image and repaints.
     *
     * @param newImage
     */
    public void setImage(Image newImage) {
        image = newImage;
        repaint();
    }

    /**
     * Gets image.
     *
     * @return image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Method to paint an image.
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if (null != image) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
        } else {
            setOpaque(true);
        }
        super.paint(g);
    }
}
