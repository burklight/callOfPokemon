package utilities.characters;

import utilities.graphics.GLargeAttack;
import java.io.Serializable;
import utilities.map.OurMap;
import utilities.graphics.Coordinate;

/**
 * Large attack template.
 */
public abstract class LargeAttack implements Serializable {

    protected volatile Coordinate coordinate;
    protected int direction;
    protected OurMap map;
    protected OurCharacter character;
    protected boolean isNew;
    protected int type, id;
    protected volatile boolean attackCollision, attackCollided;

    public LargeAttack(OurCharacter c) {
        character = c;
        coordinate = c.getCoordinate();
        direction = c.getDirection();
        map = c.getMap();
        attackCollision = false;
        attackCollided = false;
        isNew = true;
    }

    //Fabric pattern getter
    public abstract GLargeAttack getGraphics();

    //Getters
    /**
     * Gets coordinate
     *
     * @return coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Gets direction
     *
     * @return direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Gets map
     *
     * @return map
     */
    public OurMap getMap() {
        return map;
    }

    /**
     * Gets character
     *
     * @return character
     */
    public OurCharacter getCharacter() {
        return character;
    }

    /**
     * Gets isNew
     *
     * @return isNew
     */
    public boolean getIsNew() {
        return isNew;
    }

    /**
     * Gets id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets attackCollision
     *
     * @return attackCollision
     */
    public boolean getAttackCollision() {
        return attackCollision;
    }

    /**
     * Gets type
     *
     * @return type
     */
    public int getType() {
        return type;
    }

    //Setters
    /**
     * Sets coordinate
     *
     * @param coordinate
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Sets direction
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Sets map
     *
     * @param map
     */
    public void setMap(OurMap map) {
        this.map = map;
    }

    /**
     * Sets character
     *
     * @param character
     */
    public void setCharacter(OurCharacter character) {
        this.character = character;
    }

    /**
     * Sets isNew
     *
     * @param isNew
     */
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * Sets id
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets attackCollision
     *
     * @param attackCollision
     */
    public void setAttackCollision(boolean attackCollision) {
        this.attackCollision = attackCollision;
    }

    public abstract boolean hasCollision(int x, int y);

}
