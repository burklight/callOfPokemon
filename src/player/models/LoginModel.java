package player.models;

import java.util.Observable;

/**
 * Model of the login.
 */
public class LoginModel extends Observable {

    private String playerName;
    private String password;
    private int operation;

    public LoginModel() {

    }

    /**
     * Gets playerName.
     *
     * @return playerName.
     */
    public String getName() {
        return playerName;
    }

    /**
     * Gets password.
     *
     * @return password.
     */
    public String getPassword() {
        return password;
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
     * Set name.
     *
     * @param name
     */
    public void setName(String name) {
        playerName = name;
    }

    /**
     * Sets password.
     *
     * @param pass
     */
    public void setPassword(String pass) {
        password = pass;
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
     * Method that sets it changed and notifies observers when the login info
     * has been entered.
     */
    public void infoEntered() {
        setChanged();
        notifyObservers();
    }

}
