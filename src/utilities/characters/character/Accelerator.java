package utilities.characters.character;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.characters.largeAttack.LargeAttackPlasmaBall;
import utilities.characters.shortAttack.ShortAttackAirSlash;
import utilities.graphics.character.GAccelerator;
import utilities.graphics.GCharacter;
import utilities.map.OurMap;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * Class that defines Accelerator's qualities.
 *
 */
public class Accelerator extends OurCharacter {

    public Accelerator(OurMap m) throws Exception {
        super(m);
        name = "Accelerator";
        type = Comms.esper;
        maxLife = 500;
        baseLargeDamage = -20;
        baseShortDamage = -20;
        delaySpeedMovement = 350;
        delayLargeAttackSpeedMovement = 100;
        delayBetweenLargeAttack = 350;
        delayBetweenShortAttack = 2000;
        initialize();
    }

    /**
     * Checks what's inside the coordinate to see if the character will colide
     * with something.
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
            collision = obs == 1;
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
        return new GAccelerator(this);
    }

    /**
     * Gets this character's Large Attack.
     *
     * @return This character's LargeAttack
     */
    @Override
    public LargeAttack getNewLargeAttack() {
        return new LargeAttackPlasmaBall(this);
    }

    /**
     * Gets this character's Short Attack.
     *
     * @return This character's ShortAttack
     */
    @Override
    public ShortAttack getNewShortAttack() {
        return new ShortAttackAirSlash(this);
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
        if (t == Comms.magic || t == Comms.god) {
            damage *= 2;
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
        if (t == Comms.magic || t == Comms.god) {
            damage *= 2;
        }
        changeLife(damage);
    }
}
