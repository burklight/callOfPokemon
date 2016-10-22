package player.models;

import java.util.Observable;

/**
 * Model of the character selection.
 */
public class CharacterSelectionModel extends Observable {

    String characterName;

    public CharacterSelectionModel() {

    }

    /**
     * Gets characterName.
     *
     * @return characterName.
     */
    public String getName() {
        return characterName;
    }

    /**
     * Sets name.
     *
     * @param name
     */
    public void setName(String name) {
        characterName = name;
    }

    /**
     * Method that sets it changed and notifies observers when the character has
     * been selected.
     */
    public void characterSelected() {
        setChanged();
        notifyObservers();
    }

}
