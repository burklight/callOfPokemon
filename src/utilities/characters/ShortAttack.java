package utilities.characters;

import utilities.graphics.GShortAttack;
import java.io.Serializable;
import utilities.map.OurMap;
import utilities.graphics.Coordinate;

/**
 * Short attack template.
 */
public abstract class ShortAttack implements Serializable {

    protected volatile Coordinate coordinate;
    protected int direction, id, type, loopsActive;
    protected int currentLoop;
    protected OurMap map;
    protected OurCharacter character;
    protected volatile boolean isNew, ended;

    public ShortAttack(OurCharacter c) {
        character = c;
        coordinate = c.getCoordinate();
        direction = c.getDirection();
        map = c.getMap();
        currentLoop = 0;
        ended = false;
        isNew = true;
    }

    public abstract boolean hasCollision(int x, int y);

    //Getters
    public abstract GShortAttack getGraphics();

    /**
     * Gets coordinate.
     *
     * @return coordinate.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Gets direction.
     *
     * @return direction.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Gets map.
     *
     * @return map.
     */
    public OurMap getMap() {
        return map;
    }

    /**
     * Gets character.
     *
     * @return character.
     */
    public OurCharacter getCharacter() {
        return character;
    }

    /**
     * Gets id.
     *
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets ended.
     *
     * @return ended.
     */
    public boolean isEnded() {
        return ended;
    }

    /**
     * Gets isNew.
     *
     * @return isNew.
     */
    public boolean getIsNew() {
        return isNew;
    }

    /**
     * Gets type.
     *
     * @return type.
     */
    public int getType() {
        return type;
    }

    /**
     * Gets currentLoop.
     *
     * @return currentLoop.
     */
    public int getCurrentLoop() {
        return currentLoop;
    }

    /**
     * Gets loopsActive.
     *
     * @return loopsActive.
     */
    public int getLoopsActive() {
        return loopsActive;
    }

    //Setters
    /**
     * Sets coordinate.
     *
     * @param coordinate
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Sets direction.
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Sets map.
     *
     * @param map
     */
    public void setMap(OurMap map) {
        this.map = map;
    }

    /**
     * Sets character.
     *
     * @param character
     */
    public void setCharacter(OurCharacter character) {
        this.character = character;
    }

    /**
     * Sets id.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets ended.
     *
     * @param ended
     */
    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    /**
     * Sets isNew.
     *
     * @param isNew
     */
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * Sets currentLoop.
     *
     * @param currentLoop
     */
    public void setCurrentLoop(int currentLoop) {
        this.currentLoop = currentLoop;
    }

}
