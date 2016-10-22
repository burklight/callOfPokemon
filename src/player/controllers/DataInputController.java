package player.controllers;

import utilities.characters.OurCharacter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JFrame;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.communications.DataPacket;

/**
 * This class controlls the information received from the keyboard during the
 * game.
 */
public class DataInputController implements KeyListener {

    private Communications coms;
    protected OurCharacter character = null;
    protected boolean ended = false;
    private volatile boolean isMoving, isAttacking;
    private TimerTask moveTimerTask;
    private Timer movTimer;
    private final ReentrantLock mon, monQueue;
    private final Condition movementCond, largeAttackCond, shortAttackCond, blockReadQueue, blockWriteQueue;
    private final Thread movLoop, largeAtkLoop, shortAtkLoop;
    protected JFrame window;
    protected int[] pendantInputs;
    protected volatile int nextInputSendIndex, nextInputSaveIndex;
    protected int previousDirection;
    private long t1, t2, t3, t4;

    public DataInputController(JFrame w) throws Exception {
        window = w;
        nextInputSendIndex = 0;
        nextInputSaveIndex = 0;
        isMoving = false;
        isAttacking = false;
        previousDirection = Comms.down;
        mon = new ReentrantLock();
        movementCond = mon.newCondition();
        largeAttackCond = mon.newCondition();
        shortAttackCond = mon.newCondition();
        monQueue = new ReentrantLock();
        blockReadQueue = monQueue.newCondition();
        blockWriteQueue = monQueue.newCondition();
        pendantInputs = new int[20];
        movLoop = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            createMovementLoop();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        largeAtkLoop = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                createLargeAttackLoop();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
        shortAtkLoop = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                createShortAttackLoop();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
        t1 = 0;
        t2 = 0;
        t3 = 0;
        t4 = 0;
        startThreads();
        window.addKeyListener(this);
    }

    /**
     * Initializes the controller for a new game.
     */
    public void initialize() {
        nextInputSendIndex = 0;
        nextInputSaveIndex = 0;
        isMoving = false;
        isAttacking = false;
        previousDirection = Comms.down;
        pendantInputs = new int[20];
        t1 = 0;
        t2 = 0;
        t3 = 0;
        t4 = 0;
    }

    /**
     * Method to start threads.
     */
    private void startThreads() {
        movLoop.start();
        largeAtkLoop.start();
        shortAtkLoop.start();
    }

    /**
     * Listens and controls the character movements.
     *
     * @throws Exception
     */
    public void createMovementLoop() throws Exception {
        while (true) {
            mon.lock();
            try {
                movementCond.await();
                if (!isMoving) {
                    isMoving = true;
                    createMoveTimer();
                    //Haruhi is too OP, please nerf
                    movTimer.scheduleAtFixedRate(moveTimerTask, Comms.delayChangeDirection, character.getDelaySpeedMovement());
                }
            } finally {
                mon.unlock();
            }
        }
    }

    /**
     * Listens and controls the large attacks movements.
     *
     * @throws Exception
     */
    public void createLargeAttackLoop() throws Exception {
        mon.lock();
        try {
            largeAttackCond.await();
            if (!isAttacking) {
                isAttacking = true;
                setInput(Comms.largeAttack);
            }
        } finally {
            mon.unlock();
        }
    }

    /**
     * Listens and controlls the short attacks movements.
     *
     * @throws Exception
     */
    public void createShortAttackLoop() throws Exception {
        mon.lock();
        try {
            shortAttackCond.await();
            if (!isAttacking) {
                isAttacking = true;
                setInput(Comms.shortAttack);
            }
        } finally {
            mon.unlock();
        }
    }

    /**
     * Gets the next read input from the queue.
     *
     * @return input
     * @throws InterruptedException
     */
    public int getInput() throws InterruptedException {
        int input = -1;
        monQueue.lock();
        try {
            while (nextInputSendIndex == nextInputSaveIndex) {
                blockReadQueue.await();
            }
            input = pendantInputs[nextInputSendIndex];
            nextInputSendIndex = (nextInputSendIndex + 1) % 20;
            blockWriteQueue.signalAll();
        } finally {
            monQueue.unlock();
        }
        return input;
    }

    /**
     * Sets a new input into the queue.
     *
     * @param input
     * @throws InterruptedException
     */
    public void setInput(int input) throws InterruptedException {
        monQueue.lock();
        try {
            if ((nextInputSaveIndex + 1) % 20 == nextInputSendIndex) {
                nextInputSendIndex = (nextInputSendIndex + 1) % 20;
            }
            pendantInputs[nextInputSaveIndex] = input;
            nextInputSaveIndex = (nextInputSaveIndex + 1) % 20;
            blockReadQueue.signalAll();
        } finally {
            monQueue.unlock();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    /**
     * Reads from the keyboard the character commands and put them on the queue.
     *
     * @param ke
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        if (character.isAlive()) {
            char c = ke.getKeyChar();
            if (c == 'w' || c == 'W') {
                if (previousDirection != Comms.up) {
                    try {
                        setInput(Comms.changeUp);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    previousDirection = Comms.up;
                }
                mon.lock();
                try {
                    movementCond.signalAll();
                } finally {
                    mon.unlock();
                }
            } else if (c == 'd' || c == 'D') {
                if (previousDirection != Comms.right) {
                    try {
                        setInput(Comms.changeRight);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    previousDirection = Comms.right;
                }
                mon.lock();
                try {
                    movementCond.signalAll();
                } finally {
                    mon.unlock();
                }
            } else if (c == 's' || c == 'S') {
                if (previousDirection != Comms.down) {
                    try {
                        setInput(Comms.changeDown);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    previousDirection = Comms.down;
                }
                mon.lock();
                try {
                    movementCond.signalAll();
                } finally {
                    mon.unlock();
                }
            } else if (c == 'a' || c == 'A') {
                if (previousDirection != Comms.left) {
                    try {
                        setInput(Comms.changeLeft);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    previousDirection = Comms.left;
                }
                mon.lock();
                try {
                    movementCond.signalAll();
                } finally {
                    mon.unlock();
                }
            } else if (c == 'y' || c == 'Y') {
                t2 = System.currentTimeMillis();
                long delay;
                //Yeah, Haruhi again is changing the code for her own purposes
                delay = character.getDelayBetweenLargeAttack();
                if ((t2 - t1) >= delay) {
                    t1 = t2;
                    mon.lock();
                    try {
                        largeAttackCond.signalAll();
                    } finally {
                        mon.unlock();
                    }
                }
            } else if (c == 'u' || c == 'U') {
                t4 = System.currentTimeMillis();
                long delay;
                //Yeah, Haruhi again is changing the code for her own purposes
                delay = character.getDelayBetweenShortAttack();
                if ((t4 - t3) >= delay) {
                    t3 = t4;
                    mon.lock();
                    try {
                        shortAttackCond.signalAll();
                    } finally {
                        mon.unlock();
                    }
                }
            }
        }
    }

    /**
     * Detects when a key is no longer pressed and stops the action that was
     * being performed.
     *
     * @param ke
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        if (character.isAlive()) {
            char c = ke.getKeyChar();
            if (c == 'w' || c == 'd' || c == 's' || c == 'a'
                    || c == 'W' || c == 'D' || c == 'S' || c == 'A') {
                stopTimer();
                isMoving = false;
            } else if (c == 'y' || c == 'Y' || c == 'u' || c == 'U') {
                isAttacking = false;
            }
        }
    }

    /**
     * Creates a timer.
     */
    private void createMoveTimer() {
        movTimer = new Timer();
        moveTimerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    setInput(Comms.move);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    /**
     * Cancels timer.
     */
    private void stopTimer() {
        movTimer.cancel();
    }

    /**
     * Gets character.
     *
     * @return character.
     */
    public OurCharacter getCharacter() {
        return character;
    }

    /**
     * Sets character.
     *
     * @param character
     */
    public void setCharacter(OurCharacter character) {
        this.character = character;
    }

    /**
     * Sets WindowListener.
     */
    public void setWindowListener() {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                DataPacket packet = new DataPacket();
                packet.setId(coms.getNumPlayer());
                packet.setOperation(Comms.EndConnection);
                try {
                    coms.send(packet);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    /**
     * Sets coms.
     *
     * @param coms
     */
    public void setComs(Communications coms) {
        this.coms = coms;
    }
}
