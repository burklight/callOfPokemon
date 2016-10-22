package utilities.characters.character;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.characters.largeAttack.LargeAttackGrass;
import utilities.characters.shortAttack.ShortAttackGrass;
import utilities.graphics.GCharacter;
import utilities.graphics.character.GVenusaur;
import utilities.map.OurMap;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * Class that defines Venusaur's qualities.
 *
 */
public class Venusaur extends OurCharacter {

    public Venusaur(OurMap m) throws Exception {
        super(m);
        name = "Venusaur";
        type = Comms.grass;
        maxLife = 500;
        baseLargeDamage = -12;
        baseShortDamage = -18;
        delaySpeedMovement = 250;
        delayLargeAttackSpeedMovement = 100;
        delayBetweenLargeAttack = 500;
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
            if (c.getInside().getClass().getSuperclass().getSimpleName().equals("PowerUp")) {
                collision = false;
            } else {
                collision = true;
            }
        } else {
            int obs = c.getObstacle();
            collision = obs != 5 && obs != 0 && obs != Comms.numObstacles;
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
        return new GVenusaur(this);
    }

    /**
     * Gets this character's Large Attack.
     *
     * @return This character's LargeAttack
     */
    @Override
    public LargeAttack getNewLargeAttack() {
        return new LargeAttackGrass(this);
    }

    /**
     * Gets this character's Short Attack.
     *
     * @return This character's ShortAttack
     */
    @Override
    public ShortAttack getNewShortAttack() {
        return new ShortAttackGrass(this);
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
        if (t == Comms.magic || t == Comms.god || t == Comms.fire) {
            damage *= 2;
        } else if (t == Comms.water || t == Comms.grass) {
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
        if (t == Comms.magic || t == Comms.god || t == Comms.fire) {
            damage *= 2;
        } else if (t == Comms.water || t == Comms.grass) {
            damage /= 2;
        }
        changeLife(damage);
    }
}
