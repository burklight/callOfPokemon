package player.views;

import player.controllers.HallController;
import player.models.HallModel;
import player.controllers.ViewsController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import utilities.mvc.View;
import utilities.image.BottomPanel;

/**
 * This class acts as a waiting view.
 */
public class HallView implements View {

    private final ViewsController gui;
    private final JFrame window;
    private final Container container;
    private final BottomPanel panel;

    private final HallController controller;
    private final HallModel model;

    public HallView(ViewsController g) {
        gui = g;

        model = new HallModel();
        model.addObserver(this);
        controller = new HallController(model);

        window = new JFrame();
        window.setVisible(false);
        window.setTitle("Hall.");

        window.setSize(new Dimension(700, 700));
        window.setResizable(false);

        container = window.getContentPane();
        container.setLayout(new BorderLayout());

        panel = new BottomPanel();
        panel.setSize(new Dimension(700, 560));
        try {
            panel.setBackground(Color.BLACK);
            panel.setImage(ImageIO.read(new File("options/hall.jpg")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        container.add(panel, BorderLayout.CENTER);

        controller.setWindow(window);

        controller.setWindowListener();

        window.setLocationRelativeTo(null);
    }

    /**
     * Enables the game controllers and sets window visible.
     */
    public void show() {
        gui.enableGameControllers();
        window.setVisible(true);
    }

    /**
     * Closes the window.
     */
    public void close() {
        window.setVisible(false);
    }

    @Override
    public JComponent getView() {
        return null;
    }

    /**
     * Closes the hall and shows the game.
     *
     * @param o
     * @param o1
     */
    @Override
    public void update(Observable o, Object o1) {
        gui.closeHall();
        gui.showGame();
    }

    //Getter
    /**
     * Gets controler.
     *
     * @return controler.
     */
    public HallController getController() {
        return controller;
    }

    /**
     * Sets the title image
     *
     * @param i
     */
    public void setTitle(Image i) {
        window.setIconImage(i);
    }
}
