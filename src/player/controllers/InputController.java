package player.controllers;

import player.views.GameView;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import utilities.mvc.Model;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.communications.DataPacket;

/**
 * This class controls the message sending process from client to server.
 */
public class InputController extends Thread {

    protected Communications com;
    private final GameView view;
    private final Model<?> model;
    private volatile boolean enabled;
    private final ReentrantLock monitor;
    private final Condition cond;

    public InputController(Model<?> m,
            GameView v,
            Communications c) {

        com = c;
        view = v;
        model = m;
        enabled = false;
        monitor = new ReentrantLock();
        cond = monitor.newCondition();
    }

    /**
     * Sets enabled to true.
     */
    public void enable() {
        monitor.lock();
        try {
            enabled = true;
            cond.signalAll();
        } finally {
            monitor.unlock();
        }
    }

    /**
     * Sets enabled to false.
     */
    public void disable() {
        enabled = false;
    }

    /**
     * Reads the received inputs and processes them to finnally send them to the
     * server.
     */
    @Override
    public void run() {
        while (com.isWorking()) {
            monitor.lock();
            try {
                while (!enabled) {
                    try {
                        cond.await();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } finally {
                monitor.unlock();
            }
            int input = -1;
            try {
                input = view.getDataInput().getInput();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            DataPacket packet = new DataPacket();
            switch (input) {
                case Comms.changeUp:
                    packet.setOperation(Comms.ChangeDirection);
                    packet.setId(com.getNumPlayer());
                    packet.setDirection(Comms.up);
                    try {
                        com.send(packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case Comms.changeRight:
                    packet.setOperation(Comms.ChangeDirection);
                    packet.setId(com.getNumPlayer());
                    packet.setDirection(Comms.right);
                    try {
                        com.send(packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case Comms.changeDown:
                    packet.setOperation(Comms.ChangeDirection);
                    packet.setId(com.getNumPlayer());
                    packet.setDirection(Comms.down);
                    try {
                        com.send(packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case Comms.changeLeft:
                    packet.setOperation(Comms.ChangeDirection);
                    packet.setId(com.getNumPlayer());
                    packet.setDirection(Comms.left);
                    try {
                        com.send(packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case Comms.move:
                    packet.setOperation(Comms.MoveCharacter);
                    packet.setId(com.getNumPlayer());
                    try {
                        com.send(packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case Comms.largeAttack:
                    packet.setOperation(Comms.CreateNewLargeAttack);
                    packet.setId(com.getNumPlayer());
                    try {
                        com.send(packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case Comms.shortAttack:
                    packet.setOperation(Comms.CreateNewShortAttack);
                    packet.setId(com.getNumPlayer());
                    try {
                        com.send(packet);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        }
    }
}
