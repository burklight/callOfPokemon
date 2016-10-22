package utilities.image;

import javax.swing.*;
import java.awt.*;
import utilities.communications.Comms;

/**
 * Class that has the BottomButton.
 */
public class BottomButton extends JButton {

    protected Image image;
    private String name;

    public BottomButton() {

    }

    //Getters
    /**
     * Gets image.
     *
     * @return image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets name.
     *
     * @return name.
     */
    @Override
    public String getName() {
        return name;
    }

    //Setters
    /**
     * Sets image.
     *
     * @param newImage
     */
    public void setImage(Image newImage) {
        image = newImage;
        Image icon = image.getScaledInstance(Comms.buttomWidth - 50, Comms.buttomHeigth - 50, TOP);
        setIcon(new ImageIcon(icon));
    }

    /**
     * Sets name.
     *
     * @param s
     */
    @Override
    public void setName(String s) {
        name = s;
    }
}
