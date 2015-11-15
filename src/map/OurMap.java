package map;

import graphics.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import server.Comms;

public class OurMap {
   
    //basic image of the map
    protected Image map;
    //basic image of the ground
    protected Image ground;
    protected ArrayList<Coordinate> coordinates;
    protected Container container;
    protected Random rnd;
    //here is where the map will be
    protected JPanel jmap; 
    //here is where the live bar will be
    protected JPanel jbar;
 
    
    public OurMap(Container c) throws Exception{
        rnd = new Random();
        container = c;
        jmap = new JPanel();
        jmap.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.BLACK));
        jmap.setLayout(new GridLayout(Comms.obstacleColumns,Comms.obstacleRows));
        jbar=new JPanel();
        ground = ImageIO.read(new File("maps/obstacle0.png"));
        coordinates=new ArrayList<>();
        createRandomMap();
    }
    
    /**
     * This method inserts the obstacle z in the square (x,y) and save its 
     * information in coordinates.
     * @param x
     * @param y
     * @param z 
     */
    private void insert(int x, int y, int z){
        BottomPanelMap fig = new BottomPanelMap();
        fig.setImage(map);
        jmap.add(fig,x+y*Comms.obstacleColumns);
        fig.setVisible(false);
        coordinates.add(new Coordinate(x,y,new Obstacle(z,fig)));
    }
    
    /**
     * This method shows all the map.
     */
    public void show(){
        for(Coordinate c: coordinates) c.getObstacle().getBp().setVisible(true);
        jmap.setVisible(true);
    }

    /**
     * This method generates an obstacle in every square and inserts it.
    */
    private void createRandomMap(){
            for(int y = 0;y<Comms.obstacleRows;y++){
                for(int x = 0;x<Comms.obstacleColumns;x++){
                    int z=generate();
                    insert(x,y,z);
                }
            }
        verify();
        container.add(jbar, BorderLayout.NORTH);    
        container.add(jmap, BorderLayout.CENTER);
    }

    /**
     * This method generates a random obstacle and returns it's id.
     * The obstacle 0 (path) and 1 (rock) have more probabilites.
     * @return 
     */
    private int generate(){
        int i = rnd.nextInt(Comms.numObstacles);
        for(int x=0; x<2; ++x){
            if(i!=0&&i!=1) i=rnd.nextInt(Comms.numObstacles);
        }
        try {
            map = ImageIO.read(new File("maps/obstacle"+i+".png"));
        }catch(Exception ex){}
        
        return i;
     }

    /**
     * This method verifies that there are no closed paths in the map and "fix
     * it". When adjustments have been done it starts again due tue the 
     * coordinates array has changed.
     */
    private void verify(){
        boolean change=true;
        while(change){
            for(Coordinate c:coordinates){
                change=lookAround(c);
                if(change) break;
            }
        }    
    }

    /**
     * This method looks if a path coordinate (c) is surrounded by non-path
     * obstacles (up, down, left and right only without taking the borders into 
     * account) and if it is by more than two obstacles, puts path obstacles 
     * around it. Each time that makes a path change returns true.
     * @param c
     * @return 
     */
    private boolean lookAround(Coordinate c){
        boolean b=false;
        if(c.getObstacle().getObs()==0&&
                c.getX()!=0&&c.getX()!=(Comms.obstacleColumns-1)&&
                c.getY()!=0&&c.getY()!=(Comms.obstacleRows-1)){
                int count=0;
                for(int x=-1;x<=1;x+=2){
                    if(getCoordinate(c.getX()+x,c.getY()).getObstacle().getObs()!=0)
                        count++;
                    if(getCoordinate(c.getX(),c.getY()+x).getObstacle().getObs()!=0)
                        count++;
                }
                if(count>2){
                    map = ground;
                    for(int i=-1;i<=1;i++){
                        for(int n=-1;n<=1;n++){
                            if(n!=i&&n!=-i){
                                coordinates.remove(getCoordinate(c.getX()+i,c.getY()+n));
                                jmap.remove(c.getX()+i+(c.getY()+n)*Comms.obstacleColumns);
                                insert(c.getX()+i,c.getY()+n,0);
                            }
                        }
                    }
                    b=true;
                }
        }
        return b;
    }
    
    
    /**
     * This method returns the coordinate (x,y) in the map.
     * @param x
     * @param y
     * @return 
     */
    public Coordinate getCoordinate(int x,int y){
        Coordinate co = null;
        for(Coordinate c:coordinates){
            if(c.getX()==x&&c.getY()==y) co=c;
        }
        return co;
    }

    public JPanel getJmap() {
        return jmap;
    }

    public JPanel getJbar() {
        return jbar;
    }
    
}
