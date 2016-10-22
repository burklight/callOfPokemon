package utilities.characters;

import utilities.graphics.GCharacter;
import java.io.Serializable;
import java.util.Random;
import utilities.map.OurMap;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * Character template.
 */
public abstract class OurCharacter implements Serializable {

    protected volatile Coordinate coordinate;
    protected int type, life, maxLife, id, receivedShoots, inflingedShoots, kills;
    protected volatile int direction, frame;
    protected String name;
    protected OurMap map;
    protected boolean damaged, alive;
    protected int baseLargeDamage, baseShortDamage;
    protected int delaySpeedMovement, delayLargeAttackSpeedMovement;
    protected int delayBetweenLargeAttack, delayBetweenShortAttack;

    public OurCharacter(OurMap m) {
        map = m;
    }

    /**
     * This method initializes the variables of the character and puts it in the
     * map.
     */
    public void initialize() {
        life = maxLife;
        direction = Comms.down;
        damaged = false;
        alive = true;
        kills = 0;
        inflingedShoots = 0;
        receivedShoots = 0;
        //random initial position
        Random r = new Random();
        int x = 1 + r.nextInt(Comms.obstacleColumns - 2);
        int y = 1 + r.nextInt(Comms.obstacleRows - 2);
        coordinate = map.getCoordinate(x, y);
        while ((coordinate.getObstacle() != 0)
                || (coordinate.getInside() != null)) {
            x = 1 + r.nextInt(Comms.obstacleColumns - 2);
            y = 1 + r.nextInt(Comms.obstacleRows - 2);
            coordinate = map.getCoordinate(x, y);
        }
        map.getCoordinate(x, y).setInside(this);
        coordinate = map.getCoordinate(x, y);
    }

    /**
     * This method modifies the character's life.
     *
     * @param damage The variation of life.
     */
    public void changeLife(int damage) {
        increaseReceivedShoots();
        life = life + damage;
        if (life < 0) {
            life = 0;
        } else if (life > maxLife) {
            life = maxLife;
        }
    }

    /**
     * Increases receivedShoots.
     */
    public void increaseReceivedShoots() {
        this.receivedShoots++;
    }

    /**
     * Increases inflingedShoots.
     */
    public void increaseInflingedShoots() {
        this.inflingedShoots++;
    }

    /**
     * Increases kills.
     */
    public void increaseKills() {
        this.kills++;
    }

    //Fabric pattern getters
    public abstract GCharacter getGraphics();

    public abstract LargeAttack getNewLargeAttack();

    public abstract ShortAttack getNewShortAttack();

    public abstract void receiveLargeAttack(LargeAttack a);

    public abstract void receiveShortAttack(ShortAttack a);

    public abstract boolean hasCollision(int x, int y);

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
     * Gets life
     *
     * @return life
     */
    public int getLife() {
        return life;
    }

    /**
     * Gets maxLife
     *
     * @return maxLife
     */
    public int getMaxLife() {
        return maxLife;
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
     * Gets frame
     *
     * @return frame
     */
    public int getFrame() {
        return frame;
    }

    /**
     * Gets name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets damaged
     *
     * @return damaged
     */
    public boolean isDamaged() {
        return damaged;
    }

    /**
     * Gets alive
     *
     * @return alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Gets type
     *
     * @return type
     */
    public int getType() {
        return type;
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
     * Gets id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets baseLargeDamage
     *
     * @return baseLargeDamage
     */
    public int getBaseLargeDamage() {
        return baseLargeDamage;
    }

    /**
     * Gets baseShortDamage
     *
     * @return baseShortDamage
     */
    public int getBaseShortDamage() {
        return baseShortDamage;
    }

    /**
     * Gets delaySpeedMovement
     *
     * @return delaySpeedMovement
     */
    public int getDelaySpeedMovement() {
        return delaySpeedMovement;
    }

    /**
     * Gets delayLargeAttackSpeedMovement
     *
     * @return delayLargeAttackSpeedMovement
     */
    public int getDelayLargeAttackSpeedMovement() {
        return delayLargeAttackSpeedMovement;
    }

    /**
     * Gets delayBetweenLargeAttack
     *
     * @return delayBetweenLargeAttack
     */
    public int getDelayBetweenLargeAttack() {
        return delayBetweenLargeAttack;
    }

    /**
     * Gets delayBetweenShortAttack
     *
     * @return delayBetweenShortAttack
     */
    public int getDelayBetweenShortAttack() {
        return delayBetweenShortAttack;
    }

    /**
     * Gets receivedShoots
     *
     * @return receivedShoots
     */
    public int getReceivedShoots() {
        return receivedShoots;
    }

    /**
     * Gets inflingedShoots
     *
     * @return inflingedShoots
     */
    public int getInflingedShoots() {
        return inflingedShoots;
    }

    /**
     * Gets kills
     *
     * @return
     */
    public int getKills() {
        return kills;
    }

    /**
     * Sets id
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    //Haruhi is god, so we don't see the problem here.
    public boolean isGod() {
        return type == Comms.god;
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
     * Sets type
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Sets life
     *
     * @param life
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * Sets maxLife
     *
     * @param maxLife
     */
    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
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
     * Sets frame
     *
     * @param frame
     */
    public void setFrame(int frame) {
        this.frame = frame;
    }

    /**
     * Sets name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets damaged
     *
     * @param damaged
     */
    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    /**
     * Sets alive
     *
     * @param alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Sets map
     *
     * @param map
     */
    public void setMap(OurMap map) {
        this.map = map;
    }

}
