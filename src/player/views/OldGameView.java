package player.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import player.controllers.OldGameController;
import player.controllers.ViewsController;
import player.models.MovementsModel;
import player.models.OldGameModel;
import utilities.characters.LargeAttack;
import utilities.characters.ShortAttack;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;
import utilities.graphics.GCharacter;
import utilities.graphics.GraphicsGame;
import utilities.image.BottomPanel;
import utilities.map.OurMap;
import utilities.mvc.View;
import utilities.sounds.TreatSound;

/**
 * This class is the view used to rewatch and old game.
 */
public class OldGameView implements View {

    private final ViewsController gui;

    private OldGameModel model;
    private OldGameController controller;

    private GraphicsGame graphics;

    private boolean firstWatch;

    private static JFrame window;
    private static Container container;
    private final JPanel jbar;
    private JProgressBar lifes[];
    private final JPanel jmap;

    public OldGameView(ViewsController g) throws IOException {
        gui = g;

        firstWatch = true;

        window = new JFrame();
        window.setVisible(false);
        window.setTitle("Record: Call Of Pokemon!");

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
        jmap.setVisible(true);

        jbar = new JPanel();
        jbar.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.BLACK));
        jbar.setLayout(new FlowLayout());
        jbar.setVisible(true);

        container.add(jbar, BorderLayout.PAGE_START);
        container.add(jmap, BorderLayout.CENTER);

        window.setLocationRelativeTo(null);
    }

    @Override
    public JComponent getView() {
        return null;
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
     * Inizializes the map with the parameters passed.
     *
     * @param numPlayers
     * @param m
     * @param names
     * @param movements
     * @param scores
     */
    public void initialize(int numPlayers, OurMap m, String[] names, ArrayList<MovementsModel> movements, String[] scores) {
        controller = new OldGameController();
        model = new OldGameModel(numPlayers);
        model.initialize(m, names, movements, scores);
        model.addObserver(this);

        if (firstWatch) {
            lifes = new JProgressBar[model.getNumPlayers()];
            for (int i = 0; i < model.getNumPlayers(); ++i) {
                lifes[i] = new JProgressBar(0, 1);
                lifes[i].setStringPainted(true);
                lifes[i].setBorderPainted(true);
                jbar.add(lifes[i]);
            }

            container.add(jbar, BorderLayout.PAGE_START);
            container.add(jmap, BorderLayout.CENTER);

            firstWatch = false;
        }

        GCharacter graphicCharacters[] = new GCharacter[model.getNumPlayers()];
        for (int i = 0; i < model.getNumPlayers(); i++) {
            Coordinate ci = model.getCharacter(i).getCoordinate();
            model.getMap().getCoordinate(ci.getX(), ci.getY()).setInside(null);
            model.getCharacter(i).setCoordinate(model.getMovement().getCinitial());
            Coordinate co = model.getCharacter(i).getCoordinate();
            model.getMap().getCoordinate(co.getX(), co.getY()).setInside(model.getCharacter(i));
            graphicCharacters[i] = model.getCharacter(i).getGraphics();
            graphicCharacters[i].setCharacter(model.getCharacter(i));
        }
        graphics = new GraphicsGame(model.getMap(), graphicCharacters, jmap, lifes);
        for (int i = 0; i < model.getNumPlayers(); i++) {
            graphicCharacters[i].setGraphics(graphics);
        }
        graphics.startMap();
        for (int i = 0; i < Comms.NUM_PLAYERS; ++i) {
            graphics.getCharacter()[i].changeSprite();
        }
        show();

        controller.initialize(window, model);
        controller.setWindowListener();
        controller.start();
    }

    @Override
    public void update(Observable o, Object o1) {
        long n = System.currentTimeMillis();
        MovementsModel movement = model.getMovement();
        int id = movement.getId();
        int id2 = movement.getId2();
        controller.setPreviousTime(controller.getNewTime());
        controller.setNewTime(movement.getTime());
        switch (movement.getType()) {
            case "ChangeDirection":
                graphics.changeDirection(movement);
                break;
            case "MoveCharacter":
                /**
                 * ***********************************************************
                 */
                movement.setCinitial(model.getCharacter(id).getCoordinate());
                model.getCharacter(id).setCoordinate(movement.getCfinal());
                /**
                 * ***********************************************************
                 */
                graphics.moveCharacter(movement);
                break;
            case "CreateNewLargeAttack":
                /**
                 * ***********************************************************
                 */
                LargeAttack la = model.getCharacter(id).getNewLargeAttack();
                la.setCoordinate(movement.getCfinal());
                model.setLargeAttack(id2, la);
                movement.setId(id2);
                /**
                 * ***********************************************************
                 */
                graphics.setSpecificLargeAttack(movement,
                        model.getLargeAttack(id2).getGraphics());
                graphics.paintView(movement.getCfinal(),
                        graphics.getSpecificLargeAttack(id2).getActual());
                graphics.getSpecificLargeAttack(id2).changeSprite();
                break;
            case "MoveLargeAttack":
                /**
                 * ***********************************************************
                 */
                movement.setCinitial(model.getLargeAttack(id).getCoordinate());
                model.getLargeAttack(id).setCoordinate(movement.getCfinal());
                /**
                 * ***********************************************************
                 */
                graphics.moveLargeAttack(movement);
                break;
            case "EndLargeAttack":
                /**
                 * ***********************************************************
                 */
                movement.setCfinal(model.getLargeAttack(id).getCoordinate());
                /**
                 * ***********************************************************
                 */
                graphics.endLargeAttack(movement);
                break;
            case "EndShortAttack":
                graphics.endShortAttack(movement);
                break;
            case "CreateNewShortAttack":
                /**
                 * ***********************************************************
                 */
                ShortAttack sa = model.getCharacter(id).getNewShortAttack();
                sa.setCoordinate(movement.getCfinal());
                model.setShortAttack(id2, sa);
                movement.setId(id2);
                /**
                 * ***********************************************************
                 */
                graphics.setSpecificShortAttack(movement,
                        model.getShortAttack(id2).getGraphics());
                graphics.getSpecificShortAttack(id2).changeSprite();
                break;
            case "NewCharacterLife":
                /**
                 * ***********************************************************
                 */
                model.getCharacter(id).setLife(movement.getLife());
                /**
                 * ***********************************************************
                 */
                graphics.showNewCharacterLife(model.getCharacter(id).getLife(), id);
                break;
            case "CharacterDeath":
                graphics.characterDeath(movement);
                break;
            case "GameEnded":
                window.setVisible(false);
                graphics.endAll();
                gui.showMenu(gui.getUserName(), false);
                gui.victory(window, model.getScores());
                TreatSound.endSound();
                TreatSound.playSound("opening");
                break;
        }
        /**
         * *******************************************************************
         */
        n -= System.currentTimeMillis();
        if (movement.getTime() - n - controller.getPreviousTime() > 0) {
            controller.setWaitTime(movement.getTime() - n - controller.getPreviousTime());
        } else {
            controller.setWaitTime(0);
        }
        /**
         * *******************************************************************
         */
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
