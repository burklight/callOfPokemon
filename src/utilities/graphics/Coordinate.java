package utilities.graphics;

import java.io.Serializable;

/**
 * Template of a map coordinate.
 */
public class Coordinate implements Serializable {

    private int x, y, o;
    private Object inside, inside2;

    public Coordinate(int x, int y, int o, Object i) {
        this.x = x;
        this.y = y;
        this.o = o;
        inside = i;
    }

    /**
     * Gets x.
     *
     * @return x.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return y.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets o.
     *
     * @return o
     */
    public int getObstacle() {
        return o;
    }

    /**
     * Sets x.
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets y.
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets o.
     *
     * @param o
     */
    public void setObstacle(int o) {
        this.o = o;
    }

    /**
     * Gets inside.
     *
     * @return
     */
    public Object getInside() {
        return inside;
    }

    /**
     * Sets inside.
     *
     * @param inside
     */
    public void setInside(Object inside) {
        this.inside = inside;
    }

    /**
     * Gets inside2.
     *
     * @return
     */
    public Object getInside2() {
        return inside2;
    }

    /**
     * Sets inside2.
     *
     * @param inside2
     */
    public void setInside2(Object inside2) {
        this.inside2 = inside2;
    }

}
