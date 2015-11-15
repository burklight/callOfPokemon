package specifics;

import graphics.BottomPanelAttack;
import graphics.Coordinate;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import map.OurMap;
import server.Comms;

public class Attack {
    
    protected BufferedImage shortAttack[], largeAttack[];
    protected Container container;
    protected OurMap map;
    protected BottomPanelAttack largeFigure, shortFigure;
    protected JPanel jmap;
    protected int direction, largeLen, shortLen, type;
    protected Coordinate coordinate;
    protected volatile int frame;
    protected volatile boolean endLarge;
    
    public void shortRangeAttack(int d, Coordinate c){}
    
    public void largeRangeAttack(int d, Coordinate c){
        direction = d;
        coordinate = c;
        int x, y;
        endLarge=false;
        showLarge();
        do{
            x = coordinate.getX();
            y = coordinate.getY();
            if(direction == Comms.up) y=y-1;
            else if(direction == Comms.right) x=x+1;
            else if(direction == Comms.down) y=y+1;
            else if(direction == Comms.left) x=x-1;
            
            move(largeFigure);
        }while((x>=0 && x<Comms.obstacleColumns && y>=0 && y<Comms.obstacleRows) 
                && !hasCollision(x,y));
        endLarge=true;
    
    }
    
    
    /**
     * This method sets the basic variables of the attack of the pokemon
     * @param c
     * @param m 
     */
    protected void insert(Container c, OurMap m){
        map = m;
        container = c;
        largeFigure = new BottomPanelAttack();
        shortFigure = new BottomPanelAttack();
        jmap = m.getJmap();
    }
    
    /**
     * This method starts the animation of the large range attack
     */
    private void showLarge(){
        largeFigure.setVisible(true);
        while(endLarge){
            try {
                largeFigure.setImage(combine(largeAttack[frame],
                        map.getCoordinate(coordinate.getX(),
                        coordinate.getY()).getObstacle().getBf()));
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
            ++frame;
            frame = frame%largeLen;
        }
    }
    
     /**
     * This method combines the two images passed as a parameter (player and
     * obstacle) and return this combination (combination)
     * @param player
     * @param obstacle
     * @return combinedImage
     */
    private BufferedImage combine(BufferedImage player, BufferedImage obstacle){
        BufferedImage combinedImage = new BufferedImage(player.getWidth()-1, 
            player.getHeight(),BufferedImage.TYPE_INT_ARGB);
        BufferedImage destiny = new BufferedImage(player.getWidth(), 
            player.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gg = destiny.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double) player.getWidth() / obstacle.getWidth(), 
                (double) player.getHeight() / obstacle.getHeight());
        gg.drawRenderedImage(obstacle, at);
        Graphics g = combinedImage.getGraphics();
        g.drawImage(destiny, -1, 0, null ); 
        g.drawImage(player, -1, 0, null );
        return combinedImage;
    }
    
    private synchronized void move(BottomPanelAttack figure){
        int x = coordinate.getX();
        int y = coordinate.getY();
        int gridCoordinateAux = x + y*Comms.obstacleColumns;
        int auxx = x;
        int auxy = y;
        
        if(direction == Comms.up) y=y-1;
        else if(direction == Comms.right) x=x+1;
        else if(direction == Comms.down) y=y+1;
        else if(direction == Comms.left) x=x-1;
        
        int gridCoordinate = x + y*Comms.obstacleColumns;
        
        //removes the content of the square in which the attack was and 
        //fills it with it's natural obstacle instead
        jmap.remove(gridCoordinateAux);
        jmap.add(map.getCoordinate(auxx,auxy).getObstacle().getBp(),gridCoordinateAux);
        
        //check if the character does not go out of the screen and if it will
        //have any collision with an obstacle 
        if((x>=0 && x<Comms.obstacleColumns && y>=0 && y<Comms.obstacleRows) &&
                !hasCollision(x,y)){
            figure.setVisible(false);
            
            //removes the conent of the new square and fills it with the 
            //combined image of the pokemon and the obstacle instead
            jmap.remove(gridCoordinate);
            jmap.add(figure, gridCoordinate);
            //new coordinates and destiny image
            coordinate = new Coordinate(x,y,map.getCoordinate(x,y).getObstacle());
            if(direction == Comms.up) figure.setImage(combine(
                    largeAttack[frame],map.getCoordinate(x,y).getObstacle().getBf()));
            
            figure.setVisible(true);
        }
        
    }
    
     /**
     * This method return true if the character will have a collision in the
     * next move and false if not
     * @param x
     * @param y
     * @return 
     */
    private boolean hasCollision(int x, int y){
        boolean collision;
        Coordinate c = map.getCoordinate(x, y);
        collision = (c.getObstacle().getObs() != 0)&&
                (c.getObstacle().getObs() != type);
        return collision;
    }
    
}
