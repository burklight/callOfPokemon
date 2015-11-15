package graphics;

import server.Comms;

public class Coordinate {
    
    private int x,y;
    private Obstacle obs;

    public Coordinate(int x, int y, Obstacle obstacle) {
        this.x = x;
        this.y = y;
        obs=obstacle;
    }

    public int getGridCoordinate(){
        return (x + y*Comms.obstacleColumns);
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Obstacle getObstacle() {
        return obs;
    }    

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setObstacle(Obstacle obstacle) {
        obs = obstacle;
    }

}
