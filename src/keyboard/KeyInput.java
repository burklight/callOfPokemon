package keyboard;

import characters.OurCharacter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
import java.util.Timer;
import server.Comms;

//is an action listener that the character will have
public class KeyInput implements KeyListener {
    
    private final OurCharacter poke;
    private volatile boolean isMoving;
    private TimerTask timerTask;
    private Timer timer;
    
    //each input has a character
    public KeyInput(OurCharacter c){
        poke = c;
        isMoving = false;
    }
    
    private void createTimer(){
        timer = new Timer();
        timerTask = new TimerTask(){
            @Override
            public void run(){
                poke.move();
            }
        };
    }
    
    private void stopTimer(){
        timer.cancel();
    }

    /**
     * This method creates the movement loop of the pokemon. If the key is 
     * kept pressed for less than 100ms, this method does nothing, but if is 
     * pressed for more time, the function move is executed each 
     * Comms.delaySpeedMovement ms
     */
    public synchronized void createMovementLoop(){
        if(!isMoving){
            isMoving = true;
            createTimer();
            new Thread(new Runnable(){
                @Override
                public void run(){
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) { }
                    if(isMoving) timer.scheduleAtFixedRate(timerTask, 0, Comms.delaySpeedMovement);
                }
            }).start();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    
    /**
     * Actions made by pressing a button. 
     * If the pokemon was not looking at this direction then it does it.
     * If the pokemon was looking at this direction, then it moves a square 
     * stright ahead.
     * @param ke 
     */
    @Override
   public void keyPressed(KeyEvent ke) {
        char c = ke.getKeyChar();
        if(c=='w'){
            poke.setDirection(Comms.up);
            createMovementLoop();
        }
        else if(c=='d'){
            poke.setDirection(Comms.right);
            createMovementLoop();
        }
        else if(c=='s'){
            poke.setDirection(Comms.down);
            createMovementLoop();
        }
        else if(c=='a'){
            poke.setDirection(Comms.left);
            createMovementLoop();
        }
        else if(c=='j'){
            poke.shortRangeAttack();
        }
        else if(c=='k'){
            poke.largeRangeAttack();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        stopTimer();
        isMoving = false;
    }
    
}
