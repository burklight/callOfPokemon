package utilities.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import utilities.map.OurMap;
import player.models.MovementsModel;
import utilities.communications.Comms;
import utilities.image.BottomPanel;
import utilities.image.TreatImage;

/**
 * This class contains the graphics of the game.
 */
public class GraphicsGame {

    private final JPanel jmap;
    private final JProgressBar lifes[];
    private GMap map;
    //private GPowerUp powerUp;
    private GCharacter character[];
    private final GLargeAttack largeAttack[];
    private final GShortAttack shortAttack[];

    public GraphicsGame(OurMap m, GCharacter c[], JPanel jm, JProgressBar l[]) {
        jmap = jm;
        lifes = l;
        map = new GMap(m, this);
        character = c;
        largeAttack = new GLargeAttack[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        shortAttack = new GShortAttack[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
    }

    /**
     * This method changes the directon of an specific character.
     *
     * @param m the movement that will be done.
     */
    public void changeDirection(final MovementsModel m) {
        character[m.getId()].getCharacter().setDirection(m.getDirection());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                paintView(character[m.getId()].getCharacter().getCoordinate(), character[m.getId()].getActual());
            }
        });
    }

    /**
     * This method changes the position of a character.
     *
     * @param m the movement that will be done.
     */
    public void moveCharacter(final MovementsModel m) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                paintMap(m.getCinitial());
                paintView(m.getCfinal(), character[m.getId()].getActual());
            }
        });
    }

    /**
     * Method to show the character's life.
     *
     * @param life
     * @param id
     */
    public void showNewCharacterLife(final int life, final int id) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lifes[id].setValue(life);
            }
        });

    }

    /**
     * This method changes the position of a large attack.
     *
     * @param m the movement that will be done.
     */
    public void moveLargeAttack(final MovementsModel m) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                paintMap(m.getCinitial());
                paintView(m.getCfinal(), largeAttack[m.getId()].getActual());
            }
        });
    }

    /**
     * This method shows the map at the start;
     */
    public void startMap() {

        ArrayList<Coordinate> coordinates = map.getMap().getCoordinates();
        for (Coordinate c : coordinates) {
            int gridPosition = c.getX() + c.getY() * Comms.obstacleColumns;
            BottomPanel fig = (BottomPanel) jmap.getComponent(gridPosition);
            fig.setImage(map.getObs()[c.getObstacle()]);
            fig.setVisible(true);
        }
        for (int i = 0; i < Comms.NUM_PLAYERS; ++i) {
            lifes[i].setMaximum(character[i].getCharacter().getMaxLife());
            lifes[i].setValue(character[i].getCharacter().getMaxLife());
            lifes[i].setString("P" + i + ": " + character[i].getCharacter().getName());
        }

    }

    /**
     * This method paints the specific grid coordinate of the map with it's
     * deafault value.
     *
     * @param initial: the coordinate that needs to be painted again.
     */
    public void paintMap(Coordinate initial) {

        int grid = initial.getX() + initial.getY() * Comms.obstacleColumns;
        BottomPanel fig = (BottomPanel) jmap.getComponent(grid);
        fig.setImage(map.getObs()[initial.getObstacle()]);
        jmap.add(fig, grid);
        fig.setVisible(true);

    }

    /**
     * This method paints the object in the coordinate that is needed.
     *
     * @param coordinate: the coordinate where the object will be painted.
     * @param actual: the image of the object that will be painted.
     */
    public void paintView(Coordinate coordinate, BufferedImage actual) {

        int grid = coordinate.getX() + coordinate.getY() * Comms.obstacleColumns;
        BottomPanel fig = (BottomPanel) jmap.getComponent(grid);
        BufferedImage background = map.getObs()[coordinate.getObstacle()];
        BufferedImage image = TreatImage.combine(actual, background);
        fig.setImage(image);
        fig.setVisible(true);

    }

    /**
     * This method stop showing the large attack animation.
     *
     * @param m : the movmenet that will be done.
     */
    public void endLargeAttack(final MovementsModel m) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                largeAttack[m.getId()].end();
                paintMap(m.getCfinal());
            }
        });

    }

    /**
     * This method stop showing the short attack animation.
     *
     * @param m : the movmenet that will be done.
     */
    public void endShortAttack(final MovementsModel m) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                shortAttack[m.getId()].end();
                paintMap(m.getCfinal());
            }
        });

    }

    /**
     * This method stop the character animation and puts a grave instead.
     *
     * @param m : the movmenet that will be done.
     */
    public void characterDeath(final MovementsModel m) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                character[m.getId()].die(m.getCfinal());
                Coordinate c = m.getCfinal();
                int gridPosition = c.getX() + c.getY() * Comms.obstacleColumns;
                BottomPanel fig = (BottomPanel) jmap.getComponent(gridPosition);
                fig.setImage(map.getObs()[Comms.numObstacles]);
                fig.setVisible(true);
            }
        });

    }

    /**
     * Ends all the graphics.
     */
    public void endAll() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (GLargeAttack l : largeAttack) {
                    if (l != null) {
                        l.end();
                    }
                }
                for (GShortAttack s : shortAttack) {
                    if (s != null) {
                        s.end();
                    }
                }
                for (GCharacter c : character) {
                    if (c != null) {
                        c.die(c.getCharacter().getCoordinate());
                    }
                }
            }
        });

    }

    //Getters
    /**
     * Gets map.
     *
     * @return map.
     */
    public GMap getMap() {
        return map;
    }

    /**
     * Gets character.
     *
     * @return character.
     */
    public GCharacter[] getCharacter() {
        return character;
    }

    /**
     * Gets largeAttack.
     *
     * @return largeAttack.
     */
    public GLargeAttack[] getLargeAttack() {
        return largeAttack;
    }

    /**
     * Gets largeAttack[id].
     *
     * @param id
     * @return largeAttack[id].
     */
    public GLargeAttack getSpecificLargeAttack(int id) {
        return largeAttack[id];
    }

    /**
     * Gets shortAttack.
     *
     * @return shortAttack.
     */
    public GShortAttack[] getShortAttack() {
        return shortAttack;
    }

    /**
     * Gets shortAttack[id].
     *
     * @param id
     * @return shortAttack[id].
     */
    public GShortAttack getSpecificShortAttack(int id) {
        return shortAttack[id];
    }

    //Setters
    /**
     * Sets map.
     *
     * @param map
     */
    public void setMap(GMap map) {
        this.map = map;
    }

    /**
     * Sets character.
     *
     * @param character
     */
    public void setCharacter(GCharacter[] character) {
        this.character = character;
    }

    /**
     * Gets the idintifier from the model then sets the given largeAttack's
     * graphics to the array largeAttack[id].
     *
     * @param m
     * @param largeAttack
     */
    public void setSpecificLargeAttack(MovementsModel m, GLargeAttack largeAttack) {
        int id = m.getId();
        this.largeAttack[id] = largeAttack;
        this.largeAttack[id].setGraphics(this);
    }

    /**
     * Gets the idintifier from the model then sets the given shortAttack's
     * graphics to the array shortAttack[id].
     *
     * @param m
     * @param shortAttack
     */
    public void setSpecificShortAttack(MovementsModel m, GShortAttack shortAttack) {
        int id = m.getId();
        this.shortAttack[id] = shortAttack;
        this.shortAttack[id].setGraphics(this);
    }

}
