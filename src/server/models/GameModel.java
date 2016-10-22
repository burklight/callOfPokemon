package server.models;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import utilities.map.OurMap;
import player.models.MovementsModel;
import server.controllers.MovementsController;
import utilities.communications.Comms;

/**
 * Model of the game.
 *
 */
public class GameModel {

    protected OurMap map;
    protected MovementsController mover;
    protected OurCharacter[] characters;
    protected String[] usersName;
    protected LargeAttack[] largeAttacks;
    protected boolean[] largeAttacksOn;
    protected ShortAttack[] shortAttacks;
    protected boolean[] shortAttacksOn;
    protected String[] charactersString;
    protected Random r;
    protected ArrayBlockingQueue<MovementsModel> movementQueue;
    protected volatile int numCharactersAlive;
    private int idGame;
    private int idNextLargeAttack;
    private int idNextShortAttack;
    private long startTime;
    private volatile boolean ended;

    public GameModel() {
        map = new OurMap();
        r = new Random();
        idNextLargeAttack = 0;
        idNextShortAttack = 0;
        ended = false;
        idGame = 0;
        numCharactersAlive = Comms.NUM_PLAYERS;
        characters = new OurCharacter[Comms.NUM_PLAYERS];
        charactersString = new String[Comms.NUM_PLAYERS];
        usersName = new String[Comms.NUM_PLAYERS];
        largeAttacks = new LargeAttack[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        largeAttacksOn = new boolean[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        shortAttacks = new ShortAttack[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        shortAttacksOn = new boolean[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        movementQueue = new ArrayBlockingQueue<>(Comms.MovementQueueCapacity);
    }

    /**
     * Creats a random map.
     */
    public void createMap() {
        map.createRandomMap(r.nextInt(Comms.numMaps));
        mover = new MovementsController(this, map);
    }

    /**
     * Initializes all the variables.
     */
    public void initialize() {
        map = new OurMap();
        r = new Random();
        idNextLargeAttack = 0;
        idNextShortAttack = 0;
        ended = false;
        idGame = 0;
        numCharactersAlive = Comms.NUM_PLAYERS;
        characters = new OurCharacter[Comms.NUM_PLAYERS];
        charactersString = new String[Comms.NUM_PLAYERS];
        usersName = new String[Comms.NUM_PLAYERS];
        largeAttacks = new LargeAttack[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        largeAttacksOn = new boolean[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        shortAttacks = new ShortAttack[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        shortAttacksOn = new boolean[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        movementQueue.clear();
        mover = null;
    }

    /**
     * Creates a new largeAttack according to the character.
     *
     * @param id identifier of the character.
     */
    public void createLargeAttack(int id) {
        if (characters[id].isAlive()) {
            largeAttacks[idNextLargeAttack] = characters[id].getNewLargeAttack();
            largeAttacks[idNextLargeAttack].setId(idNextLargeAttack);
            largeAttacksOn[idNextLargeAttack] = true;
            mover.executeLargeAttack(largeAttacks[idNextLargeAttack]);
            idNextLargeAttack = (idNextLargeAttack + 1) % Comms.NUM_LARGE_ATTACKS_IN_SCREEN;
        }
    }

    /**
     * Creates a new shortAttack according to the character.
     *
     * @param id identifier of the character.
     */
    public void createShortAttack(int id) {
        if (characters[id].isAlive()) {
            shortAttacks[idNextShortAttack] = characters[id].getNewShortAttack();
            shortAttacks[idNextShortAttack].setId(idNextShortAttack);
            shortAttacksOn[idNextShortAttack] = true;
            mover.executeShortAttack(shortAttacks[idNextShortAttack]);
            idNextShortAttack = (idNextShortAttack + 1) % Comms.NUM_SHORT_ATTACKS_IN_SCREEN;
        }
    }

    /**
     * Changes the direction of a character.
     *
     * @param id identifier of the character.
     * @param direction
     * @throws Exception
     */
    public void changeDirection(int id, int direction) throws Exception {
        OurCharacter c = characters[id];
        if (c.isAlive()) {
            mover.changeDirection(c, direction);
        }
    }

    /**
     * Moves a character.
     *
     * @param id identifier of the character.
     * @throws InterruptedException
     * @throws IOException
     */
    public void moveCharacter(int id) throws InterruptedException, IOException {
        OurCharacter c = characters[id];
        if (c.isAlive()) {
            mover.moveCharacter(c);
        }
    }

    /**
     * Kills the character.
     *
     * @param id
     * @throws InterruptedException
     */
    public void kill(int id) throws InterruptedException {
        mover.kill(id);
    }

    /**
     * Method to select a character.
     *
     * @param id
     * @throws Exception
     */
    public void selectCharacter(int id) throws Exception {
        Class<?> clazz = Class.forName("utilities.characters.character." + charactersString[id]);
        Constructor<?> ctor = clazz.getConstructor(OurMap.class);
        Object object = ctor.newInstance(new Object[]{map});
        characters[id] = (OurCharacter) object;
        characters[id].setId(id);
        //sets the initial position into a movement
        MovementsModel m = new MovementsModel();
        m.setCinitial(characters[id].getCoordinate());
        m.setType("InitialPosition");
        m.setId(id);
        m.setTime(0);
        m.setDirection(Comms.down);
        movementQueue.add(m);
    }

    //getters ad setters
    /**
     * Sets largeAttacksOn[id] false.
     *
     * @param id
     */
    public void setLargeAttackOff(int id) {
        largeAttacksOn[id] = false;
    }

    /**
     * Sets charactersString[id].
     *
     * @param id
     * @param s
     */
    public void setCharacterString(int id, String s) {
        System.out.println(s + " selected");
        charactersString[id] = s;
    }

    /**
     * Sets usersName[id].
     *
     * @param id
     * @param n
     */
    public void setUsersName(int id, String n) {
        usersName[id] = n;
    }

    /**
     * Gets largeAttacksOn[id].
     *
     * @param id
     * @return largeAttacksOn[id].
     */
    public boolean isLargeAttackOn(int id) {
        return largeAttacksOn[id];
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
     * Gets characters[id].
     *
     * @param id
     * @return characters[id].
     */
    public OurCharacter getCharacter(int id) {
        return characters[id];
    }

    /**
     * Gets usersName[id].
     *
     * @param id
     * @return usersName[id].
     */
    public String getUsersName(int id) {
        return usersName[id];
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
     * Gets numCharactersAlive.
     *
     * @return numCharactersAlive.
     */
    public int getNumCharactersAlive() {
        return numCharactersAlive;
    }

    /**
     * Gets charactersString[id].
     *
     * @param id
     * @return charactersString[id].
     */
    public String getCharactersString(int id) {
        return charactersString[id];
    }

    /**
     * Sets numCharactersAlive.
     *
     * @param numCharactersAlive
     */
    public void setNumCharactersAlive(int numCharactersAlive) {
        this.numCharactersAlive = numCharactersAlive;
    }

    /**
     * Gets movementQueue.
     *
     * @return movementQueue.
     */
    public ArrayBlockingQueue<MovementsModel> getMovementQueue() {
        return movementQueue;
    }

    /**
     * Sets ended.
     *
     * @param ended
     */
    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    /**
     * Gets idGame.
     *
     * @return idGame.
     */
    public int getIdGame() {
        return idGame;
    }

    /**
     * Sets idGame.
     *
     * @param idGame
     */
    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    /**
     * Gets ended.
     *
     * @return ended.
     */
    public boolean isEnded() {
        return ended;
    }

    /**
     * Gets startTime.
     *
     * @return startTime.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets startTime.
     *
     * @param startTime
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
