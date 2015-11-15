package characters;

import graphics.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import map.OurMap;
import server.Comms;
import specifics.Attack;

public class OurCharacter extends Thread{
    
    //basic image of the pokemon
    protected BufferedImage sprite;
    //array of images of the pokemon in diferent positions
    private BufferedImage imageDown[], imageUp[], imageLeft[], imageRight[];
    private BottomPanelCharacter figure;
    private Container container;
    private Coordinate coordinate;
    private OurMap map;
    private JPanel jmap, jbar;
    protected int type;
    //progress bar that shows the life percentaje of the pokemon
    private JProgressBar life;
    private String pokemonName;
    //specific attacks of the pokemon
    protected Attack attack;
    //direction in which the pokemon is looking to
    private volatile int direction = Comms.down; 
    //frame of the pokemon animation
    private volatile int frame = 0; 
 
    /**
     * This method sets the basic variables of the character and settle it down
     * into the map
     * @param c @param m 
     */
    protected void insert(Container c, OurMap m){
        map = m;
        container = c;
        figure = new BottomPanelCharacter();
        pokemonName= getClass().getName().substring(11);
        jmap = m.getJmap();
        jbar = m.getJbar();
        life = new JProgressBar(0,100);
        life.setValue(100);
        life.setStringPainted(true);
        life.setBorderPainted(true);
        jbar.add(new JLabel(pokemonName+":"));
        jbar.add(life);
        initialize();
    }
    
    public void show(){
        figure.setVisible(true);
        jmap.setVisible(true);
    }
    
    
    /**
     * This method settle the character in a valid random square (this means
     * a normal path square or an equal type square, taking into account that 
     * it can't appear in a corner square)
     */
    private void initialize(){
        Random r = new Random();
        int x = 1+r.nextInt(Comms.obstacleColumns-2);
        int y = 1+r.nextInt(Comms.obstacleRows-2);
        coordinate = map.getCoordinate(x,y);
        while((coordinate.getObstacle().getObs() != 0)&&
                (coordinate.getObstacle().getObs() != type)){            
            x = 1+r.nextInt(Comms.obstacleColumns-2);
            y = 1+r.nextInt(Comms.obstacleRows-2);
            coordinate = map.getCoordinate(x,y); 
        }
        jmap.remove(coordinate.getGridCoordinate());
        figure.setImage(combine(imageDown[0],
                map.getCoordinate(x,y).getObstacle().getBf()));
        jmap.add(figure, coordinate.getGridCoordinate());
        figure.setVisible(false);
        
        start();
    }
    
    /**
     * This method generates the images that will be used to represent the
     * character
     * @param image 
     */
    protected void setImages(BufferedImage image){
        imageDown=new BufferedImage[4];
        imageUp=new BufferedImage[4];
        imageLeft=new BufferedImage[4];
        imageRight=new BufferedImage[4];
        for(int i=0;i<4;i++){
            imageDown[i]=image.getSubimage(63*i,10,53,53);
            imageLeft[i] = image.getSubimage(63*i,74,53,53);
            imageRight[i] = image.getSubimage(63*i,138,53,53);
            imageUp[i] = image.getSubimage(63*i,202,53,53);
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
    
    /**
     * This method updates the character direction and frame which let the 
     * character change its direction between frames
     */
    @Override
    public void run(){
        while(true){
            if(direction==Comms.up) lookUp(frame);
            else if(direction==Comms.right) lookRight(frame);
            else if(direction==Comms.down) lookDown(frame);
            else if(direction==Comms.left) lookLeft(frame);
            ++frame;
            frame=frame%4;
        }
    }
    
    /**
     * This method set the direction (d) of the character
     * @param d 
     */
    public void setDirection(int d){
        direction = d;
    }
    
    public int getDirection(){
        return direction;
    }
            
    /**
     * All the methods named look plus the character direction set the animation
     * of the character looking to this direction. The parameter i corresponds 
     * to the frame in which the character is.
     * @param i 
     */
    private void lookDown(int i){
        figure.setVisible(true);
        try {
            figure.setImage(combine(imageDown[i],
                    map.getCoordinate(coordinate.getX(),
                    coordinate.getY()).getObstacle().getBf()));
            Thread.sleep(100);
        } catch (InterruptedException ex) {}
    }
    
    private void lookLeft(int i){
        figure.setVisible(true);
        try {
            figure.setImage(combine(imageLeft[i],
                    map.getCoordinate(coordinate.getX(),
                    coordinate.getY()).getObstacle().getBf()));
            Thread.sleep(100);
        } catch (InterruptedException ex) {}
    }
    
    private void lookRight(int i){
        figure.setVisible(true);
        try {
            figure.setImage(combine(imageRight[i],
                    map.getCoordinate(coordinate.getX(),
                    coordinate.getY()).getObstacle().getBf()));
            Thread.sleep(100);
        } catch (InterruptedException ex) {}
    }
    
    private void lookUp(int i){
        figure.setVisible(true);
        try {
            figure.setImage(combine(imageUp[i],
                    map.getCoordinate(coordinate.getX(),
                    coordinate.getY()).getObstacle().getBf()));
            Thread.sleep(100);
        } catch (InterruptedException ex) {}
    }
    
    /**
     * This method is used to move the character. Every time that this method
     * is called the character moves to the position it is looking at.
     */
    public synchronized void move(){
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
        
        //check if the character does not go out of the screen and if it will
        //have any collision with an obstacle 
        if((x>=0 && x<Comms.obstacleColumns && y>=0 && y<Comms.obstacleRows) &&
                !hasCollision(x,y)){
            figure.setVisible(false);
            
            //removes the content of the square in which the character was and 
            //fills it with it's natural obstacle instead
            jmap.remove(gridCoordinateAux);
            jmap.add(map.getCoordinate(auxx,auxy).getObstacle().getBp(),gridCoordinateAux);
            //removes the conent of the new square and fills it with the 
            //combined image of the pokemon and the obstacle instead
            jmap.remove(gridCoordinate);
            jmap.add(figure, gridCoordinate);
            //new coordinates and destiny image
            coordinate = new Coordinate(x,y,map.getCoordinate(x,y).getObstacle());
            if(direction == Comms.up) figure.setImage(combine(
                    imageUp[frame],map.getCoordinate(x,y).getObstacle().getBf()));
            else if(direction == Comms.right) figure.setImage(combine(
                    imageRight[frame],map.getCoordinate(x,y).getObstacle().getBf()));
            else if(direction == Comms.down) figure.setImage(combine(
                    imageDown[frame],map.getCoordinate(x,y).getObstacle().getBf()));
            else if(direction == Comms.left) figure.setImage(combine(
                    imageLeft[frame],map.getCoordinate(x,y).getObstacle().getBf()));
            
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
    
    /**
     * This method does the short range attack of the pokemon
     */
    public void shortRangeAttack(){
        attack.shortRangeAttack(direction, coordinate);
    }
    
    /**
     * This method does the large range attack of the pokemon
     */
    public void largeRangeAttack(){
        attack.largeRangeAttack(direction, coordinate);
    }
}
