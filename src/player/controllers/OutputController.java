package player.controllers;

import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import player.views.GameView;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import utilities.map.OurMap;
import utilities.mvc.Model;
import player.models.MovementsModel;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.communications.DataPacket;
import utilities.graphics.Coordinate;

/**
 * This class controls the received info from the server during a game.
 */
public class OutputController extends Thread {

    protected Model model;
    protected Communications com;
    private final GameView view;
    private final Timer[] timersLargeAttack;
    protected boolean ended = false;
    private volatile boolean enabled;
    private final ReentrantLock monitor;
    private final Condition cond;

    public OutputController(Model model, GameView v, Communications com) {
        this.model = model;
        this.com = com;
        timersLargeAttack = new Timer[Comms.NUM_LARGE_ATTACKS_IN_SCREEN];
        view = v;
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
     * Sets enable to false.
     */
    public void disable() {
        enabled = false;
    }

    /**
     * Reads the data received from the server and processes it. Then adds it to
     * the queue of movements of the client.
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
            MovementsModel movement = new MovementsModel();
            DataPacket packet = new DataPacket();
            try {
                packet = com.receive();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            int id = packet.getId();
            final int id2 = packet.getId2();
            switch (packet.getOperation()) {
                case Comms.MoveCharacter:
                    movement.setType("MoveCharacter");
                    movement.setId(packet.getId());
                    movement.setCinitial(model.getCharacter(packet.getId()).getCoordinate());
                    movement.setCfinal((Coordinate) packet.getData());
                    model.getCharacter(packet.getId()).setCoordinate(movement.getCfinal());
                    model.addMovement(movement);
                    break;
                case Comms.ChangeDirection:
                    movement.setType("ChangeDirection");
                    movement.setId(packet.getId());
                    movement.setDirection(packet.getDirection());
                    model.getCharacter(packet.getId()).setDirection(packet.getDirection());
                    model.addMovement(movement);
                    break;
                case Comms.SendMap:
                    OurMap map = (OurMap) packet.getData();
                    model.setMap(map);
                    break;
                case Comms.SendCharacter:
                    if (packet.getId() == com.getNumPlayer()) {
                        view.getDataInput().setCharacter((OurCharacter) packet.getData());
                    }
                    OurCharacter c = (OurCharacter) packet.getData();
                    c.setMap(model.getMap());
                    model.setCharacter(packet.getId(), c);
                    break;
                case Comms.CreateNewLargeAttack:
                    LargeAttack la = model.getCharacter(id).getNewLargeAttack();
                    la.setCoordinate((Coordinate) packet.getData());
                    model.addLargeAttack(id2, la);
                    model.setLargeAttackOn(id2);
                    movement.setType("CreateNewLargeAttack");
                    movement.setId(id2);
                    movement.setCfinal((Coordinate) packet.getData());
                    //this code control the lifetime of the large attack (to avoid graphics errors due to lost packets)
                    startTimerLargeAttack(id2);
                    model.addMovement(movement);
                    break;
                case Comms.MoveLargeAttack:
                    //if the attack exists, it is moved
                    if (model.isLargeAttackOn(packet.getId())) {
                        movement.setType("MoveLargeAttack");
                        movement.setId(packet.getId());
                        movement.setCinitial(model.getLargeAttack(packet.getId()).getCoordinate());
                        movement.setCfinal((Coordinate) packet.getData());
                        model.getLargeAttack(packet.getId()).setCoordinate((Coordinate) packet.getData());
                        model.addMovement(movement);
                    } //if not, the client requests the server to send again the attack
                    else {
                        try {
                            reSendLargeAttack(packet.getId());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
                case Comms.EndLargeAttack:
                    movement.setType("EndLargeAttack");
                    movement.setId(packet.getId());
                    movement.setCfinal(model.getLargeAttack(packet.getId()).getCoordinate());
                    model.setLargeAttackOff(packet.getId());
                    stopTimerLargeAttack(packet.getId());
                    model.addMovement(movement);
                    break;
                case Comms.CreateNewShortAttack:
                    ShortAttack sa = model.getCharacter(packet.getId()).getNewShortAttack();
                    sa.setCoordinate((Coordinate) packet.getData());
                    model.addShortAttack(id2, sa);
                    model.setShortAttackOn(id2);
                    movement.setType("CreateNewShortAttack");
                    movement.setId(id2);
                    movement.setCfinal((Coordinate) packet.getData());
                    model.addMovement(movement);
                    break;
                case Comms.EndShortAttack:
                    movement.setType("EndShortAttack");
                    movement.setId(packet.getId());
                    movement.setCfinal(model.getShortAttack(packet.getId()).getCoordinate());
                    model.setShortAttackOff(packet.getId());
                    model.addMovement(movement);
                    break;
                case Comms.NewCharacterLife:
                    movement.setType("NewCharacterLife");
                    movement.setId(packet.getId());
                    model.getCharacter(packet.getId()).setLife(packet.getId2());
                    model.addMovement(movement);
                    break;
                case Comms.CharacterDeath:
                    model.getCharacter(packet.getId()).setAlive(false);
                    model.setCharacterDeath(packet.getId());
                    movement.setType("CharacterDeath");
                    movement.setId(packet.getId());
                    movement.setCfinal((Coordinate) packet.getData());
                    model.addMovement(movement);
                    break;
                case Comms.LinkStart:
                    model.start();
                    break;
                case Comms.GameEnded:
                    movement.setType("GameEnded");
                    stopAllLargeAttackTimers();
                    model.addMovement(movement);
                    break;
            }
        }

    }

    /**
     * Starts the timer of an specific LargeAttack.
     *
     * @param id
     */
    public void startTimerLargeAttack(final int id) {
        timersLargeAttack[id] = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                DataPacket packetToSend = new DataPacket();
                packetToSend.setOperation(Comms.IsLargeAttackOn);
                packetToSend.setId(id);
                try {
                    com.send(packetToSend);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        timersLargeAttack[id].scheduleAtFixedRate(timerTask, 5000, 100);
    }

    /**
     * Cancels the timer of an specific largeAttack.
     *
     * @param id
     */
    public void stopTimerLargeAttack(final int id) {
        timersLargeAttack[id].cancel();
    }

    /**
     * Stops all the largeAttacks that are running.
     */
    public void stopAllLargeAttackTimers() {
        for (int i = 0; i < Comms.NUM_LARGE_ATTACKS_IN_SCREEN; i++) {
            if (model.isLargeAttackOn(i)) {
                stopTimerLargeAttack(i);
            }
        }
    }

    /**
     * Resends LargeAttack.
     *
     * @param id
     * @throws Exception
     */
    public void reSendLargeAttack(int id) throws Exception {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.ReSendLargeAttack);
        packetToSend.setId(id);
        com.send(packetToSend);
    }
}
