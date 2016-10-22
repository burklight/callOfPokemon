package player.controllers;

import player.models.MenuModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.communications.DataPacket;

/**
 * This class controls the menu options.
 */
public class MenuController {

    private ViewsController gui;

    private JFrame window;
    private JButton start, records, watch, instructions, changePass;
    private final MenuModel model;
    private Communications coms;
    private String user;

    public MenuController(MenuModel m) {
        model = m;
        start = new JButton();
        records = new JButton();
        watch = new JButton();
        instructions = new JButton();
    }

    /**
     * Listener to start playing a game.
     */
    public void setStartListener() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DataPacket packet = new DataPacket();
                packet.setOperation(Comms.StartGameRequest);
                packet.setData(user);
                try {
                    coms.send(packet);
                    packet = coms.receive();
                    model.setOperation(packet.getOperation());
                    if (packet.getOperation() == Comms.StartGame) {
                        ArrayList usables = (ArrayList) packet.getData();
                        int numPlayer = packet.getId();
                        coms.setNumPlayer(numPlayer);
                        model.setData(usables);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                model.infoEntered();
            }
        };
        start.addActionListener(a);
    }

    /**
     * Listener to get scores.
     */
    public void setRecordsListener() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DataPacket packet = new DataPacket();
                packet.setOperation(Comms.WatchRecordsRequest);
                packet.setData(user);
                try {
                    coms.send(packet);
                    packet = coms.receive();
                    model.setOperation(packet.getOperation());
                    model.setData(packet.getData());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                model.infoEntered();
            }
        };
        records.addActionListener(a);
    }

    /**
     * Listener to watch old games.
     */
    public void setWatchListener() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DataPacket packet = new DataPacket();
                packet.setOperation(Comms.WatchOldGamesRequest);
                packet.setData(user);
                try {
                    coms.send(packet);
                    packet = coms.receive();
                    model.setOperation(packet.getOperation());
                    model.setData(packet.getData());
                    model.setId(packet.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                model.infoEntered();
            }
        };
        watch.addActionListener(a);
    }

    /**
     * Sets windowListener.
     */
    public void setWindowListener() {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                DataPacket packet = new DataPacket();
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

    /**
     * Listener to get the game instructions.
     */
    public void setInstructionsListener() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                model.setOperation(Comms.Instructions);
                model.infoEntered();
            }
        };
        instructions.addActionListener(a);
    }

    /**
     * Listener to change the password.
     */
    public void setChangePassListener() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DataPacket packet = new DataPacket();
                packet.setOperation(Comms.ChangePassword);
                String u[] = new String[2];
                u[0] = user;
                JPasswordField pf = new JPasswordField();
                int i = gui.newPassword(window, pf, "Introduce your new password.");
                if (i == JOptionPane.OK_OPTION) {
                    u[1] = new String(pf.getPassword());
                    packet.setData(u);
                    try {
                        coms.send(packet);
                        packet = coms.receive();
                        model.setOperation(packet.getOperation());
                    } catch (Exception ex) {
                    }
                    model.infoEntered();
                }
            }
        };
        changePass.addActionListener(a);
    }

    /**
     * Listener to get the result of a current game played. (It is not a button)
     */
    public void hear() {
        DataPacket packet = new DataPacket();
        packet.setOperation(Comms.SendScoresRequest);
        try {
            coms.send(packet);
            for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
                packet = coms.receive();
                if (packet.getId() == coms.getNumPlayer()) {
                    model.setOperation(packet.getOperation());
                    model.setData(packet.getData());
                    model.setId(packet.getId2());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.infoEntered();
    }

    /**
     * Listener to save or not a game. (Appears at the end of a game at the
     * winners menu)
     *
     * @param idGame
     * @param question
     */
    public void sendTreatRecord(int idGame, int question) {
        DataPacket packet = new DataPacket();
        if (question == JOptionPane.YES_OPTION) {
            packet.setOperation(Comms.SavedRecord);
        } else if (question == JOptionPane.NO_OPTION) {
            packet.setOperation(Comms.RemoveRecord);
        }
        packet.setData(user);
        packet.setId2(idGame);
        try {
            coms.send(packet);
            packet = coms.receive();
            switch (packet.getOperation()) {
                case Comms.SavedRecord:
                    model.setOperation(Comms.SavedRecord);
                    model.infoEntered();
                    break;
                case Comms.Error:
                    model.setOperation(Comms.ErrorInSaving);
                    model.infoEntered();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Getters
    /**
     * Gets
     *
     * @return
     */
    public JButton getStart() {
        return start;
    }

    /**
     * Gets records.
     *
     * @return records.
     */
    public JButton getRecords() {
        return records;
    }

    /**
     * Gets watch.
     *
     * @return watch.
     */
    public JButton getWatch() {
        return watch;
    }

    /**
     * Gets window.
     *
     * @return window.
     */
    public JFrame getWindow() {
        return window;
    }

    /**
     * Gets coms.
     *
     * @return coms.
     */
    public Communications getComs() {
        return coms;
    }

    //Setters
    /**
     * Sets start.
     *
     * @param start
     */
    public void setStart(JButton start) {
        this.start = start;
    }

    /**
     * Sets records.
     *
     * @param records
     */
    public void setRecords(JButton records) {
        this.records = records;
    }

    /**
     * Sets watch.
     *
     * @param watch
     */
    public void setWatch(JButton watch) {
        this.watch = watch;
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
     * Sets user.
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets window.
     *
     * @param window
     */
    public void setWindow(JFrame window) {
        this.window = window;
    }

    /**
     * Sets instructions.
     *
     * @param instructions
     */
    public void setInstructions(JButton instructions) {
        this.instructions = instructions;
    }

    /**
     * Sets changePass.
     *
     * @param changePass
     */
    public void setChangePass(JButton changePass) {
        this.changePass = changePass;
    }

    /**
     * Sets the views controller in order to make the pop-ups.
     *
     * @param gui
     */
    public void setGui(ViewsController gui) {
        this.gui = gui;
    }

}
