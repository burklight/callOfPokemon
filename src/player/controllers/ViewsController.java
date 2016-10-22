package player.controllers;

import java.awt.Image;
import java.io.File;
import player.views.CharacterSelectionView;
import player.views.GameView;
import player.views.HallView;
import player.views.LoginView;
import player.views.MenuView;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import player.models.MovementsModel;
import player.views.OldGameView;
import utilities.communications.Comms;
import utilities.mvc.Model;
import utilities.communications.Communications;
import utilities.map.OurMap;
import utilities.sounds.TreatSound;

/**
 * This class controls the diferent views and their controllers.
 *
 * @param <T>
 */
public class ViewsController<T> {

    private final Model model;
    private String name, userName;
    private volatile boolean characterSelected;
    private final LoginView lv;
    private final MenuView mv;
    private final CharacterSelectionView csv;
    private final HallView hv;
    private final GameView gv;
    private final OldGameView ogv;

    private final ImageIcon victory, great, error, defeated, score, question, unlocked;
    private final Image cop;

    public ViewsController(Model m) throws Exception {
        name = null;
        userName = null;
        characterSelected = false;
        model = m;

        score = new ImageIcon(ImageIO.read(new File("options/scores.png")));
        victory = new ImageIcon(ImageIO.read(new File("options/victory.png")));
        defeated = new ImageIcon(ImageIO.read(new File("options/defeated.png")));
        error = new ImageIcon(ImageIO.read(new File("options/wrong.png")));
        great = new ImageIcon(ImageIO.read(new File("options/great.png")));
        question = new ImageIcon(ImageIO.read(new File("options/question.png")));
        cop = ImageIO.read(new File("options/cop.png"));
        unlocked = new ImageIcon(ImageIO.read(new File("charactersSelection/unlocked.png")));

        lv = new LoginView(this);
        lv.setTitle(cop);
        lv.show();

        mv = new MenuView(this);
        mv.setTitle(cop);

        csv = new CharacterSelectionView(this);
        csv.setTitle(cop);

        hv = new HallView(this);
        hv.setTitle(cop);

        gv = new GameView(this);
        gv.setTitle(cop);
        gv.setModel(model);

        ogv = new OldGameView(this);
        ogv.setTitle(cop);
    }

    /**
     * Shows hall.
     */
    public void showHall() {
        hv.show();
    }

    /**
     * Closes hall.
     */
    public void closeHall() {
        hv.close();
    }

    /**
     * Shows CaracterSelection.
     *
     * @param usables
     */
    public void showCharacterSelection(ArrayList usables) {
        csv.show(usables);
    }

    /**
     * Shows menu.
     *
     * @param name
     * @param hear
     */
    public void showMenu(String name, boolean hear) {
        mv.show(name, hear);
    }

    /**
     * Shows game.
     */
    public void showGame() {
        gv.show();
    }

    /**
     * Disables controllers.
     */
    public void disableGameControllers() {
        gv.disableControllers();
    }

    /**
     * Enables controllers.
     */
    public void enableGameControllers() {
        gv.enableControllers();
    }

    /**
     * Shows a recorded old game.
     *
     * @param numPlayers : The number of players of that game.
     * @param m : The map of that game.
     * @param names : The names of the characters of that game.
     * @param movements : The movements done on that game.
     * @param scores
     */
    public void showOldGame(int numPlayers, OurMap m, String[] names, ArrayList<MovementsModel> movements, String[] scores) {
        ogv.initialize(numPlayers, m, names, movements, scores);
        ogv.show();
    }

    /**
     * Reinitializes game.
     *
     * @param coms
     * @throws Exception
     */
    public void reInitializeGame(Communications coms) throws Exception {
        gv.initializeInput();
        gv.initializeControllers(coms);
        gv.initializeModel();
        TreatSound.endSound();
        TreatSound.playSound("opening");
    }

    //Getters
    /**
     * Gets name.
     *
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets characterSelected.
     *
     * @return characterSelected.
     */
    public boolean isCharacterSelected() {
        return characterSelected;
    }

    /**
     * Gets userName.
     *
     * @return userName.
     */
    public String getUserName() {
        return userName;
    }

    //Setters
    /**
     * Sets userName.
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Sets name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets characterSelected.
     *
     * @param characterSelected
     */
    public void setCharacterSelected(boolean characterSelected) {
        this.characterSelected = characterSelected;
    }

    /**
     * Initializes controller communications.
     *
     * @param com
     * @throws Exception
     */
    public void initializeControllerCommunications(Communications com) throws Exception {
        lv.getController().setComs(com);
        mv.getController().setComs(com);
        csv.getController().setComs(com);
        hv.getController().setComs(com);
        gv.initializeControllers(com);
    }

    //Messages
    /**
     * Shows an information message.
     *
     * @param s
     * @param t
     * @param window
     */
    public void infoMessage(JFrame window, String s, String t) {
        JOptionPane.showMessageDialog(window, s, t, JOptionPane.INFORMATION_MESSAGE, great);
    }

    /**
     * Shows an error message.
     *
     * @param s
     * @param window
     */
    public void errorMessage(JFrame window, String s) {
        JOptionPane.showMessageDialog(window, s, "Oh no!", JOptionPane.INFORMATION_MESSAGE, error);
    }

    /**
     * Shows a question message.
     *
     * @param s
     * @param t
     * @param window
     * @return
     */
    public int questionMessage(JFrame window, String s, String t) {
        return JOptionPane.showConfirmDialog(window, s, t, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, question);
    }

    public int newPassword(JFrame window, JPasswordField pf, String t) {
        return JOptionPane.showConfirmDialog(window, pf, t, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, question);
    }

    /**
     * Shows a message that appears right when the player wins a game
     * congratulating him and informing about the scores. It also changes the
     * music to the theme "victory".
     *
     * @param scores
     * @param window
     */
    public void victory(JFrame window, String[] scores) {
        TreatSound.endSound();
        TreatSound.playSound("victory");
        String message = "Congratulations! You have won! \n \nScores: \n \n";
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            message += scores[i] + "\n";
        }
        JOptionPane.showMessageDialog(window, message, "VICTORY!", JOptionPane.INFORMATION_MESSAGE, victory);
    }

    /**
     * Shows a message that appears right when the player loses a game informing
     * him about the scores. It also changes the music to the theme
     * "teamrocket".
     *
     * @param window
     * @param scores
     */
    public void defeated(JFrame window, String[] scores) {
        TreatSound.endSound();
        TreatSound.playSound("teamrocket");
        String message = "Oh no! You have been defeated! \n \nScores: \n \n";
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            message += scores[i] + "\n";
        }
        JOptionPane.showMessageDialog(window, message, "DEFEATED!", JOptionPane.INFORMATION_MESSAGE, defeated);
    }

    /**
     * Shows the scores.
     *
     * @param window
     * @param scores
     */
    public void showScores(JFrame window, String[] scores) {
        String message1 = "General Scores: \n";
        int i = 0;
        while (i < Comms.numScoresGeneral) {
            if (!scores[i].equals("There aren't scores...")) {
                message1 += scores[i] + "\n";
            }
            i++;
        }
        String message2 = "Your Scores: \n";
        int j = Comms.numScoresGeneral;
        while (j < Comms.numScoresUser + Comms.numScoresGeneral) {
            if (!scores[j].equals("There aren't scores...")) {
                message2 += scores[j] + "\n";
            }
            j++;
        }
        JOptionPane.showMessageDialog(window, message1, "General scores!", JOptionPane.INFORMATION_MESSAGE, score);
        JOptionPane.showMessageDialog(window, message2, "Personal scores!", JOptionPane.INFORMATION_MESSAGE, score);
    }

    /**
     * Method that shows a message informing that a character has been unlocked.
     *
     * @param window
     */
    public void characterUnlocked(JFrame window) {
        JOptionPane.showMessageDialog(window, null, "Character unlocked!!", JOptionPane.INFORMATION_MESSAGE, unlocked);
    }

}
