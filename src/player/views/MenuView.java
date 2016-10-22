package player.views;

import player.controllers.MenuController;
import player.models.MenuModel;
import player.controllers.ViewsController;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utilities.map.OurMap;
import utilities.mvc.View;
import utilities.communications.Comms;

/**
 * This class is the view of the principal menu of options.
 */
public class MenuView implements View {

    private final ViewsController gui;
    private String userName = null;

    private final JFrame window;
    private final Container container;
    private final JLabel Lname;
    private final JPanel panel0, panel1;
    private final JButton start, records, watch, instructions, changePass;

    private final MenuController controller;
    private final MenuModel model;

    public MenuView(ViewsController g) throws IOException {
        gui = g;

        model = new MenuModel();
        model.addObserver(this);
        controller = new MenuController(model);
        controller.setGui(gui);

        window = new JFrame();
        window.setVisible(false);
        window.setTitle("Choose between these options.");

        window.setSize(new Dimension(350, 220));
        window.setResizable(false);

        container = window.getContentPane();
        container.setLayout(new BorderLayout());

        panel0 = new JPanel();
        panel0.setLayout(new BorderLayout());
        panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());

        Lname = new JLabel();
        Lname.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        Lname.setPreferredSize(new Dimension(350, 30));
        Lname.setHorizontalAlignment(JLabel.CENTER);
        start = new JButton();
        start.setPreferredSize(new Dimension(230, 25));
        start.setText("Start new game.");
        records = new JButton();
        records.setPreferredSize(new Dimension(230, 25));
        records.setText("See your scores.");
        watch = new JButton();
        watch.setPreferredSize(new Dimension(230, 25));
        watch.setText("Watch old games.");
        instructions = new JButton();
        instructions.setPreferredSize(new Dimension(230, 25));
        instructions.setText("Instructions.");
        changePass = new JButton();
        changePass.setPreferredSize(new Dimension(230, 25));
        changePass.setText("Change password.");

        panel0.add(Lname, BorderLayout.CENTER);
        panel1.add(start);
        panel1.add(records);
        panel1.add(watch);
        panel1.add(instructions);
        panel1.add(changePass);

        controller.setWindow(window);
        controller.setStart(start);
        controller.setRecords(records);
        controller.setWatch(watch);
        controller.setInstructions(instructions);
        controller.setChangePass(changePass);

        controller.setWindowListener();
        controller.setStartListener();
        controller.setRecordsListener();
        controller.setWatchListener();
        controller.setInstructionsListener();
        controller.setChangePassListener();

        container.add(panel0, BorderLayout.NORTH);
        container.add(panel1, BorderLayout.CENTER);

        window.setLocationRelativeTo(null);
    }

    /**
     * Sets the window visible and activates the menu.
     *
     * @param name
     * @param hear
     */
    public void show(String name, boolean hear) {
        if (userName == null) {
            userName = name;
            controller.setUser(userName);
            Lname.setText(name);
        }
        window.setVisible(true);
        if (hear) {
            controller.hear();
        }
    }

    @Override
    public JComponent getView() {
        return null;
    }

    /**
     * Gets controller.
     *
     * @return controller.
     */
    public MenuController getController() {
        return controller;
    }

    /**
     * This method handles all the possibles options and proceeds to do what the
     * player has request. If required, show a message informing.
     *
     * @param o
     * @param o1
     */
    @Override
    public void update(Observable o, Object o1) {
        switch (model.getOperation()) {
            case Comms.NoGamesAvaliable:
                gui.errorMessage(window, "There are no games available");
                break;
            case Comms.StartGame:
                window.setVisible(false);
                gui.showCharacterSelection((ArrayList) model.getData());
                break;
            case Comms.WatchRecords:
                String scores[] = (String[]) model.getData();
                gui.showScores(window, scores);
                break;
            case Comms.WatchOldGames:
                ArrayList record = (ArrayList) model.getData();
                treatOldGame(record);
                break;
            case Comms.Instructions:
                gui.infoMessage(window, Comms.GameInstructions, "Instructions");
                gui.infoMessage(window, Comms.GameControls, "Controls");
                break;
            case Comms.PasswordChanged:
                gui.infoMessage(window, "Your password was changed correctly!", "Password chanded");
                break;
            case Comms.Error:
                gui.errorMessage(window, "Your password couldn't be changed!");
            case Comms.GameLost:
                String s[] = (String[]) model.getData();
                gui.disableGameControllers();
                gui.defeated(window, s);
                try {
                    gui.reInitializeGame(controller.getComs());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case Comms.GameWinned:
                String t[] = (String[]) model.getData();
                int idGame = model.getId();
                gui.disableGameControllers();
                gui.victory(window, t);
                int r = gui.questionMessage(window, "Do you want to save the game?", "Game saving.");
                controller.sendTreatRecord(idGame, r);
                try {
                    gui.reInitializeGame(controller.getComs());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case Comms.ErrorInSaving:
                gui.errorMessage(window, "Your game couldn't be saved!");
                break;
            case Comms.SavedRecord:
                gui.infoMessage(window, "Your game has been saved!", "Game saved!");
                break;
        }
    }

    /**
     * Treats the array received when asked for a record.
     *
     * @param record
     */
    private void treatOldGame(ArrayList record) {
        if (record.isEmpty()) {
            gui.errorMessage(window, "You don't have any old game saved.");
        } else {
            int numPlayers = model.getId();
            OurMap m = (OurMap) record.remove(0);
            String score[] = new String[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                score[i] = (String) record.remove(0);
            }
            String names[] = new String[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                names[i] = (String) record.remove(0);
            }
            ArrayList movements = new ArrayList<>();
            while (!record.isEmpty()) {
                movements.add(record.remove(0));
            }
            window.setVisible(false);
            gui.showOldGame(numPlayers, m, names, movements, score);
        }
    }

    //Messages and option panes shown.
    /**
     * Sets userName.
     *
     * @param s
     */
    public void setUserName(String s) {
        userName = s;
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
