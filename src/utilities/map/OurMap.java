package utilities.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * This class contains the map template.
 */
public class OurMap implements Serializable {

    protected ArrayList<Coordinate> coordinates;
    protected Random rnd;
    private int type;

    public OurMap() {
        rnd = new Random();
        coordinates = new ArrayList<>();
    }

    /**
     * This method inserts the obstacle z in the square (x,y) and save its
     * information in coordinates.
     *
     * @param x
     * @param y
     * @param z
     */
    private void insert(int x, int y, int z) {
        coordinates.add(new Coordinate(x, y, z, null));
    }

    /**
     * This method generates an obstacle in every square and inserts it.
     *
     * @param type
     */
    public void createRandomMap(int type) {
        this.type = type;
        for (int y = 0; y < Comms.obstacleRows; y++) {
            for (int x = 0; x < Comms.obstacleColumns; x++) {
                int z = generate();
                insert(x, y, z);
            }
        }
        verify();
    }

    /**
     * This method generates a random obstacle and returns it's id. The obstacle
     * 0 (path) and 1 (rock) have more probabilites.
     *
     * @return
     */
    private int generate() {
        int i = rnd.nextInt(Comms.numObstacles);
        for (int x = 0; x < 2; ++x) {
            if (i != 0 && i != 1) {
                i = rnd.nextInt(Comms.numObstacles);
            }
        }
        return i;
    }

    /**
     * This method verifies that there are no closed paths in the map and "fix
     * them". When adjustments have been done it starts again due tue the
     * coordinates array has changed.
     */
    private void verify() {
        boolean change = true;
        while (change) {
            for (Coordinate c : coordinates) {
                change = lookAround(c);
                if (change) {
                    break;
                }
            }
        }
    }

    /**
     * This method looks if a path coordinate (c) is surrounded by non-path
     * obstacles (up, down, left and right only without taking the borders into
     * account) and if it is by more than two obstacles, puts path obstacles
     * around it. Each time that makes a path change returns true.
     *
     * @param c
     * @return
     */
    private boolean lookAround(Coordinate c) {
        boolean b = false;
        if (c.getObstacle() == 0
                && c.getX() != 0 && c.getX() != (Comms.obstacleColumns - 1)
                && c.getY() != 0 && c.getY() != (Comms.obstacleRows - 1)) {
            int count = 0;
            for (int x = -1; x <= 1; x += 2) {
                if (getCoordinate(c.getX() + x, c.getY()).getObstacle() != 0) {
                    count++;
                }
                if (getCoordinate(c.getX(), c.getY() + x).getObstacle() != 0) {
                    count++;
                }
            }
            if (count > 2) {
                for (int i = -1; i <= 1; i++) {
                    for (int n = -1; n <= 1; n++) {
                        if (n != i && n != -i) {
                            coordinates.remove(getCoordinate(c.getX() + i, c.getY() + n));
                            insert(c.getX() + i, c.getY() + n, 0);
                        }
                    }
                }
                b = true;
            }
        }
        return b;
    }

    //Getters
    /**
     * Gets coordinate.
     *
     * @param x
     * @param y
     * @return co (coordinate)
     */
    public Coordinate getCoordinate(int x, int y) {
        Coordinate co = null;
        for (Coordinate c : coordinates) {
            if (c.getX() == x && c.getY() == y) {
                co = c;
            }
        }
        return co;
    }

    /**
     * Gets coordinates.
     *
     * @return coordinates.
     */
    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    /**
     * Gets type.
     *
     * @return type.
     */
    public int getType() {
        return type;
    }

    //Setters
    /**
     * Sets coordinates.
     *
     * @param coordinates
     */
    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Sets type.
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

}
