package player.views;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import utilities.mvc.View;
import player.controllers.CharacterSelectionController;
import player.controllers.ViewsController;
import player.models.CharacterSelectionModel;
import utilities.communications.Comms;
import utilities.image.BottomButton;
import utilities.image.TreatImage;

/**
 * This class defines the features of the character selection's view.
 */
public class CharacterSelectionView implements View {

    private final ViewsController gui;
    private final JFrame window;
    private final Container container;
    private final JPanel panel;
    private final JScrollPane scroll;
    private int numChar;
    private boolean characterSelected;
    private final CharacterSelectionModel model;
    private final CharacterSelectionController controller;
    private final BottomButton characters[];
    private ArrayList usableChar;

    /**
     * This constructor sets the view.
     *
     * @param g : The graphic user interface.
     * @throws java.io.IOException
     */
    public CharacterSelectionView(ViewsController g) throws IOException {
        gui = g;

        model = new CharacterSelectionModel();
        model.addObserver(this);
        controller = new CharacterSelectionController(model);

        window = new JFrame();
        window.setVisible(false);
        window.setTitle("Select your character.");

        window.setSize(new Dimension(700, 700));
        window.setResizable(false);

        container = window.getContentPane();

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        scroll = new JScrollPane();
        scroll.setViewportView(panel);
        scroll.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.BLACK));

        characters = new BottomButton[Comms.numCharacters];
        for (int i = 0; i < Comms.numCharacters; i++) {
            characters[i] = new BottomButton();
            panel.add(characters[i], i);
        }

        numChar = 0;
        characterSelected = false;
        usableChar = new ArrayList<>();

        controller.setWindow(window);

        controller.setWindowListener();

        container.add(scroll);

        window.setLocationRelativeTo(null);
    }

    /**
     * This method is made to set all the character buttons.
     */
    private void addCharacters() {
        for (int i = 0; i < Comms.numCharacters; ++i) {
            addCharacter(Comms.characters[i]);
        }
        numChar = 0;
    }

    /**
     * This method sets a specific character in a position on the character
     * selection pane.
     *
     * @param name : The name of the specific character.
     */
    private void addCharacter(String name) {
        try {
            BufferedImage image = ImageIO.read(new File("charactersSelection/" + name + ".png"));
            characters[numChar].setName(name);
            characters[numChar].setBackground(java.awt.Color.LIGHT_GRAY);
            if (!usableChar.contains(name)) {
                image = TreatImage.setSiluete(image);
                characters[numChar].setBackground(java.awt.Color.DARK_GRAY);
            }
            characters[numChar].setImage(image);
            controller.setSpecificButton(characters[numChar], numChar);
            characters[numChar].setVisible(true);
            ++numChar;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Shows the view after adding listeners to the unlocked characters.
     *
     * @param usables
     */
    public void show(ArrayList usables) {
        ArrayList lastUsables = usableChar;
        usableChar = usables;
        addCharacters();
        controller.setButtomListeners(usableChar);
        window.setVisible(true);
        if (!lastUsables.isEmpty() && lastUsables.size() != usableChar.size()) {
            gui.characterUnlocked(window);
        }
    }

    @Override
    public JComponent getView() {
        return null;
    }

    /**
     * Updates the view after selecting a character.
     *
     * @param o
     * @param o1
     */
    @Override
    public void update(Observable o, Object o1) {
        characterSelected = true;
        gui.setName(model.getName());
        gui.setCharacterSelected(characterSelected);
        window.setVisible(false);
        gui.showHall();
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
     * Gets panel.
     *
     * @return panel.
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Gets numChar.
     *
     * @return numChar.
     */
    public int getNumChar() {
        return numChar;
    }

    /**
     * Gets controller.
     *
     * @return controller.
     */
    public CharacterSelectionController getController() {
        return controller;
    }

    //Setters
    /**
     * Sets numChar.
     *
     * @param numChar
     */
    public void setNumChar(int numChar) {
        this.numChar = numChar;
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
