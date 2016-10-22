package player.controllers;

import player.models.HallModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.communications.DataPacket;

/**
 * This class controls the hall.
 */
public class HallController {

    private JFrame window;
    private HallModel model;
    private Communications coms;

    public HallController(HallModel m) {
        model = m;
    }

    /**
     * Sets windowListener.
     */
    public void setWindowListener() {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                DataPacket packet = new DataPacket();
                packet.setId(coms.getNumPlayer());
                packet.setOperation(Comms.EndConnection);
                try {
                    coms.send(packet);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    //Getters
    /**
     * Gets window.
     *
     * @return window.
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Gets model.
     *
     * @return model.
     */
    public HallModel getModel() {
        return model;
    }

    //Setters
    /**
     * Sets window.
     *
     * @param window
     */
    public void setWindow(JFrame window) {
        this.window = window;
    }

    /**
     * Sets model.
     *
     * @param model
     */
    public void setModel(HallModel model) {
        this.model = model;
    }

    /**
     * Sets coms.
     *
     * @param coms
     */
    public void setComs(Communications coms) {
        this.coms = coms;
    }

}
