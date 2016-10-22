package player.controllers;

import player.models.CharacterSelectionModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.communications.DataPacket;
import utilities.image.BottomButton;

/**
 * This class controls the charater selection menu.
 */
public class CharacterSelectionController {

    private JFrame window;
    private final BottomButton buttons[];
    private final CharacterSelectionModel model;
    private Communications coms;

    public CharacterSelectionController(CharacterSelectionModel m) {
        model = m;
        buttons = new BottomButton[Comms.numCharacters];
    }

    /**
     * Sets button listeners to those buttons that refer to unlocked characters.
     *
     * @param usableChar
     */
    public void setButtomListeners(ArrayList usableChar) {
        for (final BottomButton b : buttons) {
            for (ActionListener al : b.getActionListeners()) {
                b.removeActionListener(al);
            }
            ActionListener a = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    model.setName(b.getName());
                    model.characterSelected();
                    DataPacket packetToSend = new DataPacket();
                    packetToSend.setOperation(Comms.CharacterSelected);
                    packetToSend.setId(coms.getNumPlayer());
                    packetToSend.setData(b.getName());
                    try {
                        coms.send(packetToSend);
                    } catch (Exception ex) {
                        Logger.getLogger(CharacterSelectionController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            MouseListener m = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent me) {

                }

                @Override
                public void mousePressed(MouseEvent me) {

                }

                @Override
                public void mouseReleased(MouseEvent me) {

                }

                /**
                 * Sets the background yellow when the mouse goes above an
                 * unlocked character
                 *
                 * @param me
                 */
                @Override
                public void mouseEntered(MouseEvent me) {
                    b.setBackground(Color.YELLOW);
                }

                /**
                 * Sets the background gray just after the mouse leaves the
                 * square of the character.
                 *
                 * @param me
                 */
                @Override
                public void mouseExited(MouseEvent me) {
                    b.setBackground(Color.LIGHT_GRAY);
                }

            };
            if (usableChar.contains(b.getName())) {
                b.addActionListener(a);
                b.addMouseListener(m);
            }
        }
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
                }
                System.exit(0);
            }
        });
    }

    //Getters
    /**
     * Gets an specifiead button.
     *
     * @param id identifier of the button.
     * @return buttons[id].
     */
    public BottomButton getSpecificButton(int id) {
        return buttons[id];
    }

    /**
     * Gets window.
     *
     * @return window.
     */
    public JFrame getWindow() {
        return window;
    }

    //Setters
    /**
     * Sets the button b tho buttons[].
     *
     * @param b button to set.
     * @param id identifier of the button.
     */
    public void setSpecificButton(BottomButton b, int id) {
        buttons[id] = b;
    }

    /**
     * Sets coms.
     *
     * @param coms
     */
    public void setComs(Communications coms) {
        this.coms = coms;
    }

    /**
     * Sets window.
     *
     * @param window
     */
    public void setWindow(JFrame window) {
        this.window = window;
    }

}
