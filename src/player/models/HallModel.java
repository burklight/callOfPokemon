package player.models;

import java.util.Observable;

/**
 * Model of the hall.
 */
public class HallModel extends Observable {

    private boolean end;

    public HallModel() {

    }

    /**
     * Method that sets it changed and notifies oberservers.
     */
    public void infoEntered() {
        setChanged();
        notifyObservers();
    }

    /**
     * Gets end.
     *
     * @return end.
     */
    public boolean isEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

}
