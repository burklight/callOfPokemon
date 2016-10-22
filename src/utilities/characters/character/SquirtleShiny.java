package utilities.characters.character;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.characters.largeAttack.LargeAttackWater;
import utilities.characters.shortAttack.ShortAttackWater;
import utilities.graphics.GCharacter;
import utilities.graphics.character.GSquirtleShiny;
import utilities.map.OurMap;
import utilities.communications.Comms;

public class SquirtleShiny extends OurCharacter {

    public SquirtleShiny(OurMap m) throws Exception {
        super(m);
        name = "Squirtle shiny";
        type = Comms.water;
        maxLife = 100;
        baseLargeDamage = -5;
        baseShortDamage = -8;
        delaySpeedMovement = 200;
        delayLargeAttackSpeedMovement = 100;
        delayBetweenLargeAttack = 500;
        delayBetweenShortAttack = 2000;
        initialize();
    }

    @Override
    public boolean hasCollision(int x, int y) {
        boolean collision = false;
        utilities.graphics.Coordinate c = map.getCoordinate(x, y);
        if (c.getInside() != null) {
            collision = !c.getInside().getClass().getSuperclass().getSimpleName().equals("PowerUp");
        } else {
            int obs = c.getObstacle();
            collision = obs != 4 && obs != 0 && obs != Comms.numObstacles;
        }
        return collision;
    }

    @Override
    public GCharacter getGraphics() {
        return new GSquirtleShiny(this);
    }

    @Override
    public LargeAttack getNewLargeAttack() {
        return new LargeAttackWater(this);
    }

    @Override
    public ShortAttack getNewShortAttack() {
        return new ShortAttackWater(this);
    }

    @Override
    public void receiveLargeAttack(LargeAttack a) {
        int damage = a.getCharacter().getBaseLargeDamage();
        int t = a.getType();
        if (t == Comms.electric || t == Comms.grass || t == Comms.god) {
            damage *= 2;
        } else if (t == Comms.fire || t == Comms.water) {
            damage /= 2;
        }
        changeLife(damage);
    }

    @Override
    public void receiveShortAttack(ShortAttack a) {
        int damage = a.getCharacter().getBaseShortDamage();
        int t = a.getType();
        if (t == Comms.electric || t == Comms.grass || t == Comms.god) {
            damage *= 2;
        } else if (t == Comms.fire || t == Comms.water) {
            damage /= 2;
        }
        changeLife(damage);
    }

}
