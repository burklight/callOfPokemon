package player.views;

import player.controllers.OutputController;
import player.controllers.DataInputController;
import player.controllers.InputController;
import utilities.graphics.GCharacter;
import utilities.graphics.GraphicsGame;
import player.controllers.ViewsController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import utilities.mvc.Model;
import utilities.mvc.View;
import player.models.MovementsModel;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.image.BottomPanel;
import utilities.sounds.TreatSound;

/**
 * This class shows the view of the current game being played.
 */
public class GameView implements View {

    private final ViewsController gui;

    private Model model;
    private DataInputController input;
    private InputController inputController;
    private OutputController outputController;

    private boolean firstTime;
    private GraphicsGame graphics;
    private String name;

    private static JFrame window;
    private static Container container;
    private JPanel jbar;
    private JProgressBar lifes[];
    private JPanel jmap;

    public GameView(ViewsController g) {
        gui = g;

        firstTime = true;

        window = new JFrame();
        window.setVisible(false);
        window.setTitle("Call Of Pokemon!");

        window.setSize(new Dimension(Comms.windowWidth, Comms.windowHeight));
        window.setResizable(false);

        container = window.getContentPane();
        container.setLayout(new BorderLayout());

        jmap = new JPanel();
        jmap.setSize(Comms.windowWidth, Comms.windowHeight);
        jmap.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.BLACK));
        jmap.setLayout(new GridLayout(Comms.obstacleRows, Comms.obstacleColumns));
        for (int i = 0; i < Comms.obstacleColumns * Comms.obstacleRows; ++i) {
            jmap.add(new BottomPanel());
        }

        jbar = new JPanel();
        jbar.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.BLACK));
        jbar.setLayout(new FlowLayout());

        lifes = new JProgressBar[Comms.NUM_PLAYERS];
        for (int i = 0; i < Comms.NUM_PLAYERS; ++i) {
            lifes[i] = new JProgressBar(0, 1);
            lifes[i].setStringPainted(true);
            lifes[i].setBorderPainted(true);
            jbar.add(lifes[i]);
        }

        container.add(jbar, BorderLayout.PAGE_START);
        container.add(jmap, BorderLayout.CENTER);

        window.setLocationRelativeTo(null);

        try {
            input = new DataInputController(window);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        input.setWindowListener();
    }

    /**
     * Changes the sound to the theme "battle" and sets window visible.
     */
    public void show() {
        TreatSound.endSound();
        TreatSound.playSound("battle");
        window.setVisible(true);
    }

    /**
     * Initializes input.
     */
    public void initializeInput() {
        input.initialize();
    }

    /**
     * Inizializes the controllers.
     *
     * @param coms
     * @throws Exception
     */
    public void initializeControllers(Communications coms) throws Exception {
        input.setComs(coms);
        inputController = new InputController(model, this, coms);
        outputController = new OutputController(model, this, coms);
        inputController.start();
        outputController.start();
    }

    /**
     * Initializes the model.
     */
    public void initializeModel() {
        model.initialize();
    }

    /**
     * Enables controllers.
     */
    public void enableControllers() {
        inputController.enable();
        outputController.enable();
    }

    /**
     * Disables controllers.
     */
    public void disableControllers() {
        inputController.disable();
        outputController.disable();
    }

    @Override
    public JComponent getView() {
        return null;
    }

    /**
     * This method calls graphics to do all the graphic stuff.
     *
     * @param obs
     * @param obj
     */
    @Override
    public void update(Observable obs, Object obj) {
        if (firstTime) {
            gui.closeHall();
            GCharacter graphicCharacters[] = new GCharacter[Comms.NUM_PLAYERS];
            for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
                graphicCharacters[i] = model.getCharacter(i).getGraphics();
                graphicCharacters[i].setCharacter(model.getCharacter(i));
            }
            graphics = new GraphicsGame(model.getMap(), graphicCharacters, jmap, lifes);
            for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
                graphicCharacters[i].setGraphics(graphics);
            }
            graphics.startMap();
            for (int i = 0; i < Comms.NUM_PLAYERS; ++i) {
                graphics.getCharacter()[i].changeSprite();
            }
            firstTime = false;
            show();
        } else {
            MovementsModel movement = model.getMovement();
            int id = movement.getId();
            switch (movement.getType()) {
                case "ChangeDirection":
                    graphics.changeDirection(movement);
                    break;
                case "MoveCharacter":
                    graphics.moveCharacter(movement);
                    break;
                case "CreateNewLargeAttack":
                    graphics.setSpecificLargeAttack(movement,
                            model.getLargeAttack(id).getGraphics());
                    graphics.paintView(movement.getCfinal(),
                            graphics.getSpecificLargeAttack(id).getActual());
                    graphics.getSpecificLargeAttack(id).changeSprite();
                    break;
                case "MoveLargeAttack":
                    graphics.moveLargeAttack(movement);
                    break;
                case "EndLargeAttack":
                    graphics.endLargeAttack(movement);
                    break;
                case "CreateNewShortAttack":
                    graphics.setSpecificShortAttack(movement,
                            model.getShortAttack(id).getGraphics());
                    graphics.getSpecificShortAttack(id).changeSprite();
                    break;
                case "EndShortAttack":
                    graphics.endShortAttack(movement);
                    break;
                case "NewCharacterLife":
                    graphics.showNewCharacterLife(model.getCharacter(id).getLife(), id);
                    break;
                case "CharacterDeath":
                    graphics.characterDeath(movement);
                    break;
                case "GameEnded":
                    window.setVisible(false);
                    graphics.endAll();
                    firstTime = true;
                    gui.showMenu(gui.getUserName(), true);
                    break;
            }
        }
    }

    //Getters
    /**
     * Gets input.
     *
     * @return input.
     */
    public DataInputController getDataInput() {
        return input;
    }

    /**
     * Gets firstTime.
     *
     * @return firstTime.
     */
    public boolean isFirstTime() {
        return firstTime;
    }

    /**
     * Gets name.
     *
     * @return name.
     */
    public String getName() {
        return name;
    }

    //Setters
    /**
     * Sets name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets input.
     *
     * @param in
     */
    public void setInput(DataInputController in) {
        input = in;
    }

    /**
     * Sets firsTime.
     *
     * @param firstTime
     */
    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    /**
     * Sets model and adds observer.
     *
     * @param model
     */
    public void setModel(Model model) {
        this.model = model;
        model.addObserver(this);
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
