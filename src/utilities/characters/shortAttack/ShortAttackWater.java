package utilities.characters.shortAttack;

import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;
import utilities.graphics.GShortAttack;
import utilities.graphics.shortAttack.GShortAttackWater;

/**
 * This class defines ShortAttackWater's qualities.
 *
 */
public class ShortAttackWater extends ShortAttack {

    public ShortAttackWater(OurCharacter c) {
        super(c);
        type = Comms.water;
        loopsActive = 10;
    }

    /**
     * Gets the graphics of this ShortAttack.
     *
     * @return This ShortAttack's graphics
     */
    @Override
    public GShortAttack getGraphics() {
        return new GShortAttackWater(this);
    }

    /**
     * Checks what's inside the coordinate to see if the ShortAttack will
     * colide.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return false if there's no collision or true if the attack collides.
     */
    @Override
    public boolean hasCollision(int x, int y) {
        boolean collision = false;
        Coordinate c = map.getCoordinate(x, y);
        if (c.getInside2() != null) {
            collision = true;
        } else if (c.getInside() != null) {
            collision = c.getInside().getClass().getSuperclass().getSimpleName().equals("PowerUp");
        }
        return collision;
    }

}
