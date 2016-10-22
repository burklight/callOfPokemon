package utilities.characters.character;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.characters.largeAttack.LargeAttackMagicBall;
import utilities.characters.shortAttack.ShortAttackMagicSlash;
import utilities.graphics.GCharacter;
import utilities.graphics.character.GNanoha;
import utilities.map.OurMap;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * Class that defines Nanoha's qualities.
 *
 */
public class Nanoha extends OurCharacter {

    public Nanoha(OurMap m) throws Exception {
        super(m);
        name = "Nanoha";
        type = Comms.magic;
        maxLife = 250;
        baseLargeDamage = -10;
        baseShortDamage = -10;
        delaySpeedMovement = 150;
        delayLargeAttackSpeedMovement = 80;
        delayBetweenLargeAttack = 400;
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
            collision = obs == 1;
        }
        return collision;
    }

    /**
     * Gets the graphics of this character.
     *
     * @return The graphics of this character
     */
    public GCharacter getGraphics() {
        return new GNanoha(this);
    }

    /**
     * Gets this character's Large Attack.
     *
     * @return This character's LargeAttack
     */
    @Override
    public LargeAttack getNewLargeAttack() {
        return new LargeAttackMagicBall(this);
    }

    /**
     * Gets this character's Short Attack.
     *
     * @return This character's ShortAttack
     */
    @Override
    public ShortAttack getNewShortAttack() {
        return new ShortAttackMagicSlash(this);
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
        } else if (t == Comms.magic) {
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
        } else if (t == Comms.magic) {
            damage /= 2;
        }
        changeLife(damage);
    }

}
