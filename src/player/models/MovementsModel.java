package player.models;

import java.io.Serializable;
import utilities.graphics.Coordinate;

/**
 * This class is used to classify the movements of the game.
 */
public class MovementsModel implements Serializable {

    protected String type;
    protected int id, id2;
    protected int direction;
    protected int life;
    protected long time;
    protected Coordinate Cinitial;
    protected Coordinate Cfinal;

    public MovementsModel() {
        Cinitial = null;
        Cfinal = null;
    }

    public MovementsModel(String type, int id, int id2, int direction, int life, long time, Coordinate Cinitial, Coordinate Cfinal) {
        this.type = type;
        this.id = id;
        this.id2 = id2;
        this.direction = direction;
        this.life = life;
        this.time = time;
        this.Cinitial = Cinitial;
        this.Cfinal = Cfinal;
    }

    /**
     * Gets type.
     *
     * @return type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
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
     * Gets id2.
     *
     * @return id2.
     */
    public int getId2() {
        return id2;
    }

    /**
     * Sets id2.
     *
     * @param id2
     */
    public void setId2(int id2) {
        this.id2 = id2;
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
     * Gets life.
     *
     * @return life.
     */
    public int getLife() {
        return life;
    }

    /**
     * Sets life.
     *
     * @param life
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * Gets Cinitial.
     *
     * @return Cinitial.
     */
    public Coordinate getCinitial() {
        return Cinitial;
    }

    /**
     * Sets Cinitial.
     *
     * @param Cinitial
     */
    public void setCinitial(Coordinate Cinitial) {
        this.Cinitial = Cinitial;
    }

    /**
     * Gets Cfinal.
     *
     * @return Cfinal.
     */
    public Coordinate getCfinal() {
        return Cfinal;
    }

    /**
     * Sets Cfinal.
     *
     * @param Cfinal
     */
    public void setCfinal(Coordinate Cfinal) {
        this.Cfinal = Cfinal;
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
     * Sets direction.
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Gets time.
     *
     * @return time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }

}
