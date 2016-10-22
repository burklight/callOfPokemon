package player.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.swing.JFrame;
import java.util.Timer;
import player.models.MovementsModel;
import player.models.OldGameModel;
import utilities.communications.Comms;

/**
 * Controller of the OldGame. Controls when to "send" the new movement.
 */
public class OldGameController {

    private JFrame window;
    private OldGameModel model;
    private long previousTime, newTime, waitTime;
    private int counter;
    private Timer timer;
    private TimerTask clock;
    private Thread replay;

    public OldGameController() {

    }

    public void initialize(JFrame w, OldGameModel m) {
        window = w;
        model = m;
        counter = (int) model.getMovements().get(model.getMovements().size() - 1).getTime() / 1000;
        previousTime = 0;
        newTime = 0;
        waitTime = 0;
    }

    public void start() {
        replay = new Thread() {
            @Override
            public void run() {
                while (model.hasMovements()) {
                    try {
                        sleep(waitTime);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    model.newMovement();
                }
            }
        };
        startClock();
        replay.start();
    }

    /**
     * Sets windowListener.
     */
    public void setWindowListener() {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                replay.stop();
                ArrayList<MovementsModel> a = new ArrayList<>();
                MovementsModel m = new MovementsModel();
                //m.setType(Comms.GameEnded);
                m.setType("GameEnded");
                a.add(m);
                model.setMovement(a);
                model.newMovement();
                window.setVisible(false);
            }
        });
    }
    
    /**
     * Creates the clock that will show the time left;
     */
    public void startClock(){
        timer = new Timer();
        clock = new TimerTask(){

            @Override
            public void run() {
                window.setTitle("Record: Call Of Pokemon! " + (counter) + " seconds left.");
                if (counter == 0){
                    window.setTitle("Record: Call Of Pokemon! is about to end.");
                    timer.cancel();
                }
                --counter;
            }
        
        };
        timer.scheduleAtFixedRate(clock, 0, 1000);
    }

    //Getters
    public JFrame getWindow() {
        return window;
    }

    public OldGameModel getModel() {
        return model;
    }

    public long getPreviousTime() {
        return previousTime;
    }

    public long getNewTime() {
        return newTime;
    }

    //Setters
    public void setWindow(JFrame window) {
        this.window = window;
    }

    public void setModel(OldGameModel model) {
        this.model = model;
    }

    public void setPreviousTime(long previousTime) {
        this.previousTime = previousTime;
    }

    public void setNewTime(long newTime) {
        this.newTime = newTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

}
