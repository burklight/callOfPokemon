package utilities.communications;

import java.io.Serializable;

/**
 * This class is used for transmission purposes to encapsulate data.
 */
public class DataPacket implements Serializable {

    protected Object object;
    protected int controlCode;
    protected int id, id2;
    protected int direction;

    public DataPacket() {
        object = null;
    }

    /**
     * Sets object and controlCode.
     *
     * @param s
     * @param o
     */
    public DataPacket(int code, Object o) {
        object = o;
        controlCode = code;
    }

    /**
     * Sets object.
     *
     * @param o
     */
    public void setData(Object o) {
        object = o;
    }

    /**
     * Sets object.
     *
     * @return
     */
    public Object getData() {
        return object;
    }

    /**
     * Sets controlCode.
     *
     * @param s
     */
    public void setOperation(int code) {
        controlCode = code;
    }

    /**
     * Gets controlCode.
     *
     * @return
     */
    public int getOperation() {
        return controlCode;
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
     * Gets id2.
     *
     * @return
     */
    public int getId2() {
        return id2;
    }

    /**
     * Sets id2.
     *
     * @param id2
     */
    public void setId2(int id2) {
        this.id2 = id2;
    }

    /**
     * Gets direction.
     *
     * @return direction.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Sets direction.
     *
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

}
