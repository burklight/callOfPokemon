package utilities.characters.character;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.characters.largeAttack.LargeAttackSOS;
import utilities.characters.shortAttack.ShortAttackSOS;
import utilities.graphics.GCharacter;
import utilities.graphics.character.GHaruhi;
import utilities.map.OurMap;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * Class that defines Haruhi's qualities.
 *
 */
public class Haruhi extends OurCharacter {

    public Haruhi(OurMap m) throws Exception {
        super(m);
        name = "Haruhi";
        type = Comms.god;
        maxLife = 1000;
        baseLargeDamage = -5;
        baseShortDamage = -10;
        delaySpeedMovement = 100;
        delayLargeAttackSpeedMovement = 50;
        delayBetweenLargeAttack = 300;
        delayBetweenShortAttack = 1000;
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
        Coordinate c = map.getCoordinate(x, y);
        boolean collision = c.getInside() != null;
        return collision;
    }

    /**
     * Gets the graphics of this character.
     *
     * @return The graphics of this character
     */
    @Override
    public GCharacter getGraphics() {
        return new GHaruhi(this);
    }

    /**
     * Gets this character's Large Attack.
     *
     * @return This character's LargeAttack
     */
    @Override
    public LargeAttack getNewLargeAttack() {
        return new LargeAttackSOS(this);
    }

    /**
     * Gets this character's Short Attack.
     *
     * @return This character's ShortAttack
     */
    @Override
    public ShortAttack getNewShortAttack() {
        return new ShortAttackSOS(this);
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
        changeLife(damage);
    }

}
