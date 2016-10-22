package utilities.characters.largeAttack;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.graphics.GLargeAttack;
import utilities.graphics.largeAttack.GLargeAttackWater;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * This class defines LargeAttackWater's qualities.
 *
 */
public class LargeAttackWater extends LargeAttack {

    public LargeAttackWater(OurCharacter c) {
        super(c);
        type = Comms.water;
    }

    /**
     * Gets the graphics of this LargeAttack.
     *
     * @return This LargeAttack's graphics
     */
    @Override
    public GLargeAttack getGraphics() {
        return new GLargeAttackWater(this);
    }

    /**
     * Checks what's inside the coordinate to see if the LargeAttack will
     * colide.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return false if there's no collision or true if the attack collides.
     */
    @Override
    public boolean hasCollision(int x, int y) {
        Coordinate c = map.getCoordinate(x, y);
        boolean collision = c.getObstacle() == 1 || c.getInside() != null;
        if (map.getCoordinate(x, y).getInside2() != null) {
            ShortAttack a = (ShortAttack) map.getCoordinate(x, y).getInside2();
            if (a.getCharacter().getId() != character.getId()) {
                collision = true;
            }
        }
        return collision;
    }

}
