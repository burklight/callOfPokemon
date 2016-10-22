package player.models;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Observable;
import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import utilities.communications.Comms;
import utilities.map.OurMap;

/**
 * Model of the OldGame
 */
public class OldGameModel extends Observable {

    private OurMap map;
    private final OurCharacter[] characters;
    private final LargeAttack[] largeAttacks;
    private final ShortAttack[] shortAttacks;
    private String[] scores;
    private ArrayList<MovementsModel> movement;
    private int numPlayers;

    public OldGameModel(int numP) {
        numPlayers = numP;
        largeAttacks = new LargeAttack[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        shortAttacks = new ShortAttack[Comms.NUM_SHORT_ATTACKS_IN_SCREEN];
        characters = new OurCharacter[numPlayers];
        scores = new String[numPlayers];
    }

    public void initialize(OurMap m, String[] names, ArrayList<MovementsModel> movements, String s[]) {
        setMap(m);
        setCharacters(names);
        setScores(s);
        setMovement(movements);
    }

    public boolean hasMovements() {
        return !movement.isEmpty();
    }

    //Getters
    public OurMap getMap() {
        return map;
    }

    public OurCharacter getCharacter(int id) {
        return characters[id];
    }

    public LargeAttack getLargeAttack(int id) {
        return largeAttacks[id];
    }

    public ShortAttack getShortAttack(int id) {
        return shortAttacks[id];
    }

    public MovementsModel getMovement() {
        return movement.remove(0);
    }

    public ArrayList<MovementsModel> getMovements() {
        return movement;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public String[] getScores() {
        return scores;
    }

    //Setters
    public void setMap(OurMap map) {
        this.map = map;
    }

    public void setCharacters(String[] names) {
        for (int i = 0; i < names.length; ++i) {
            try {
                Class<?> c = Class.forName("utilities.characters.character." + names[i]);
                Constructor<?> ct = c.getConstructor(OurMap.class);
                Object o = ct.newInstance(new Object[]{map});
                characters[i] = (OurCharacter) o;
                characters[i].setId(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setScores(String[] s) {
        scores = s;
    }

    public void setLargeAttack(int id, LargeAttack largeAttack) {
        this.largeAttacks[id] = largeAttack;
    }

    public void setShortAttack(int id, ShortAttack shortAttack) {
        this.shortAttacks[id] = shortAttack;
    }

    public void setMovement(ArrayList<MovementsModel> movement) {
        this.movement = movement;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void newMovement() {
        setChanged();
        notifyObservers();
    }
}
