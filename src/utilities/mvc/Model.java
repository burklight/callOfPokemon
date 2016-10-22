package utilities.mvc;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import java.util.Observable;
import utilities.map.OurMap;
import player.models.MovementsModel;
import utilities.communications.Comms;

/**
 * This class has the model of the game.
 *
 * @param <T>
 */
public class Model<T> extends Observable {

    protected OurMap map;
    protected OurCharacter[] characters;
    protected LargeAttack[] largeAttacks;
    protected boolean[] largeAttacksOn;
    protected ShortAttack[] shortAttacks;
    protected boolean[] shortAttacksOn;
    protected boolean[] charactersAlive;
    protected MovementsModel movement;

    public Model() {
        characters = new OurCharacter[Comms.NUM_PLAYERS];
        charactersAlive = new boolean[Comms.NUM_PLAYERS];
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            charactersAlive[i] = true;
        }
        largeAttacks = new LargeAttack[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        largeAttacksOn = new boolean[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        shortAttacks = new ShortAttack[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        shortAttacksOn = new boolean[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        map = new OurMap();
    }

    /**
     * Initializes the model of the game.
     */
    public void initialize() {
        characters = new OurCharacter[Comms.NUM_PLAYERS];
        charactersAlive = new boolean[Comms.NUM_PLAYERS];
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            charactersAlive[i] = true;
        }
        largeAttacks = new LargeAttack[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        largeAttacksOn = new boolean[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        shortAttacks = new ShortAttack[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        shortAttacksOn = new boolean[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        map = new OurMap();
    }

    /**
     * Gets map.
     *
     * @return map.
     */
    public OurMap getMap() {
        return map;
    }

    /**
     * Sets map.
     *
     * @param map
     */
    public void setMap(OurMap map) {
        this.map = map;
    }

    /**
     * Sets characters[id].
     *
     * @param id
     * @param c
     */
    public void setCharacter(int id, OurCharacter c) {
        characters[id] = c;
    }

    /**
     * Gets characters[id].
     *
     * @return characters[id].
     */
    public OurCharacter getCharacter(int id) {
        return characters[id];
    }

    /**
     * Gets largeAttacks[id].
     *
     * @param id
     * @return largeAttacks[id].
     */
    public LargeAttack getLargeAttack(int id) {
        return largeAttacks[id];
    }

    /**
     * Adds the given largeAttack with the given identifier to the
     * largeAttacks[] array.
     *
     * @param id
     * @param a
     */
    public void addLargeAttack(int id, LargeAttack a) {
        largeAttacks[id] = a;
    }

    /**
     * Sets largeAttacksOn[id] to true.
     *
     * @param id
     */
    public void setLargeAttackOn(int id) {
        largeAttacksOn[id] = true;
    }

    /**
     * Sets largeAttacksOn[id] to false.
     *
     * @param id
     */
    public void setLargeAttackOff(int id) {
        largeAttacksOn[id] = false;
    }

    /**
     * Gets largeAttacksOn[id].
     *
     * @return largeAttacksOn[id].
     */
    public boolean isLargeAttackOn(int id) {
        return largeAttacksOn[id];
    }

    /**
     * Gets shortAttacks[id].
     *
     * @return shortAttacks[id].
     */
    public ShortAttack getShortAttack(int id) {
        return shortAttacks[id];
    }

    /**
     * Adds the given shortAttack with the given identifier to the
     * shortAttacks[]
     *
     * @param id
     * @param a
     */
    public void addShortAttack(int id, ShortAttack a) {
        shortAttacks[id] = a;
    }

    /**
     * Sets shortAttacksOn[id] to true.
     *
     * @param id
     */
    public void setShortAttackOn(int id) {
        shortAttacksOn[id] = true;
    }

    /**
     * Sets shortAttacksOn[id] to false.
     *
     * @param id
     */
    public void setShortAttackOff(int id) {
        shortAttacksOn[id] = false;
    }

    /**
     * Gets shortAttacksOn[id].
     *
     * @param id
     * @return shortAttacksOn[id].
     */
    public boolean isShortAttackOn(int id) {
        return shortAttacksOn[id];
    }

    /**
     * Sets charactersAlive[id] to false.
     *
     * @param id
     */
    public void setCharacterDeath(int id) {
        charactersAlive[id] = false;
    }

    /**
     * Gets charactersAlive[id].
     *
     * @return charactersAlive[id].
     */
    public boolean isAlive(int id) {
        return charactersAlive[id];
    }

    /**
     * Gets movement.
     *
     * @return movement.
     */
    public MovementsModel getMovement() {
        return movement;
    }

    /**
     * Sets it changed and notifies observers.
     */
    public void start() {
        setChanged();
        notifyObservers();
    }

    /**
     * Adds the given movement, sets it changed and notifies observers.
     *
     * @param m
     */
    public void addMovement(MovementsModel m) {
        movement = m;
        setChanged();
        notifyObservers();
    }

}
