package utilities.characters.character;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.characters.largeAttack.LargeAttackElectric;
import utilities.characters.shortAttack.ShortAttackElectric;
import utilities.graphics.GCharacter;
import utilities.graphics.character.GPikachu;
import utilities.map.OurMap;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * Class that defines Pikachu's qualities.
 *
 */
public class Pikachu extends OurCharacter {

    public Pikachu(OurMap m) throws Exception {
        super(m);
        name = "Pikachu";
        type = Comms.electric;
        maxLife = 100;
        baseLargeDamage = -5;
        baseShortDamage = -8;
        delaySpeedMovement = 200;
        delayLargeAttackSpeedMovement = 100;
        delayBetweenLargeAttack = 500;
        delayBetweenShortAttack = 2000;
        initialize();
    }

    /**
     * Checks what's inside the coordinate to see if a collision will happen.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return false if there's no collision or true if the player collides.
     */
    @Override
    public boolean hasCollision(int x, int y) {
        boolean collision = false;
        Coordinate c = map.getCoordinate(x, y);
        if (c.getInside() != null) {
            collision = !c.getInside().getClass().getSuperclass().getSimpleName().equals("PowerUp");
        } else {
            int obs = c.getObstacle();
            collision = obs != 3 && obs != 0 && obs != Comms.numObstacles;
        }
        return collision;
    }

    /**
     * Gets the graphics of this character.
     *
     * @return The graphics of this character
     */
    @Override
    public GCharacter getGraphics() {
        return new GPikachu(this);
    }

    /**
     * Gets this character's Large Attack.
     *
     * @return This character's LargeAttack
     */
    @Override
    public LargeAttack getNewLargeAttack() {
        return new LargeAttackElectric(this);
    }

    /**
     * Gets this character's Short Attack.
     *
     * @return This character's ShortAttack
     */
    @Override
    public ShortAttack getNewShortAttack() {
        return new ShortAttackElectric(this);
    }

    /**
     * This method checks which LargeAttack has been received and modifies the
     * life of the player accordingly.
     *
     * @param a The received LargeAttack
     */
    @Override
    public void receiveLargeAttack(LargeAttack a) {
        int damage = a.getCharacter().getBaseLargeDamage();
        int t = a.getType();
        if (t == Comms.esper || t == Comms.god) {
            damage *= 2;
        } else if (t == Comms.electric) {
            damage /= 2;
        }
        changeLife(damage);
    }

    /**
     * This method checks which ShortAttack has been received and modifies the
     * life of the player accordingly.
     *
     * @param a The received ShortAttack
     */
    @Override
    public void receiveShortAttack(ShortAttack a) {
        int damage = a.getCharacter().getBaseShortDamage();
        int t = a.getType();
        if (t == Comms.esper || t == Comms.god) {
            damage *= 2;
        } else if (t == Comms.electric) {
            damage /= 2;
        }
        changeLife(damage);
    }

}
