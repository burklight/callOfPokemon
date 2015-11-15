/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Daniel
 */
public interface Comms {
    
    public static final int windowWidth = 700;
    public static final int windowHeight = 700;
    public static final int obstacleWidth = 35;
    public static final int obstacleHeight = 35;
    public static final int obstacleRows = windowHeight/obstacleHeight;
    public static final int obstacleColumns = windowWidth/obstacleWidth;
    public static final int numObstacles = 5;
    
    //pokemon directions
    public static final int up = 1;
    public static final int right = 2;
    public static final int down = 3;
    public static final int left = 4;
    //delay between each movement of the pokemon
    public static long delaySpeedMovement = 200;
    
}
