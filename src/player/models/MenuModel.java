package player.models;

import java.util.Observable;

/**
 * Model of the menu.
 */
public class MenuModel extends Observable {

    private int operation;
    private Object data;
    private int id;

    public MenuModel() {

    }

    /**
     * Gets operation.
     *
     * @return operation.
     */
    public int getOperation() {
        return operation;
    }

    /**
     * Sets operation.
     *
     * @param operation
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * Gets data.
     *
     * @return data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Gets id.
     *
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets data.
     *
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Method that sets it changed and notifies observers when the info has been
     * entered.
     */
    public void infoEntered() {
        setChanged();
        notifyObservers();
    }

}
