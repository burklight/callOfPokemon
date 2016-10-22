package server.controllers;

import server.models.GameModel;
import utilities.characters.LargeAttack;
import utilities.characters.OurCharacter;
import utilities.characters.ShortAttack;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import utilities.map.OurMap;
import player.models.MovementsModel;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * This class controls the interaction between actions of each player in the
 * game.
 */
public class MovementsController {

    protected volatile OurMap map;
    protected volatile boolean isSomethingMoving;
    protected GameModel game;
    protected Lock monitor1;
    protected Condition condition1;

    public MovementsController(GameModel g, OurMap m) {
        map = m;
        game = g;
        isSomethingMoving = false;
        monitor1 = new ReentrantLock();
        condition1 = monitor1.newCondition();
    }

    /**
     * Algorithm to change the direction of the given character.
     *
     * @param c
     * @param direction
     * @throws Exception
     */
    public void changeDirection(OurCharacter c, int direction) throws Exception {
        monitor1.lock();
        try {
            while (isSomethingMoving) {
                condition1.await();
            }
            isSomethingMoving = true;

            c.setDirection(direction);
            //add movement to the game
            MovementsModel movement = new MovementsModel();
            movement.setTime(System.currentTimeMillis() - game.getStartTime());
            movement.setType("ChangeDirection");
            movement.setId(c.getId());
            movement.setDirection(c.getDirection());
            game.getMovementQueue().add(movement);

            isSomethingMoving = false;
            condition1.signalAll();
        } finally {
            monitor1.unlock();
        }
    }

    /**
     * Algorithm to move the character.
     *
     * @param c
     * @throws InterruptedException
     * @throws IOException
     */
    public void moveCharacter(OurCharacter c) throws InterruptedException, IOException {
        monitor1.lock();
        try {
            while (isSomethingMoving) {
                condition1.await();
            }
            isSomethingMoving = true;
            //get the necessary variables from character
            Coordinate coordinate = c.getCoordinate();
            int direction = c.getDirection();

            int auxx = coordinate.getX();
            int auxy = coordinate.getY();
            int x = auxx;
            int y = auxy;

            if (direction == Comms.up) {
                y = y - 1;
            } else if (direction == Comms.right) {
                x = x + 1;
            } else if (direction == Comms.down) {
                y = y + 1;
            } else if (direction == Comms.left) {
                x = x - 1;
            }

            //check if the character does not go out of the screen and if it will
            //have any collision with an obstacle
            if (!outOfBounds(x, y) && !c.hasCollision(x, y)) {
                map.getCoordinate(x, y).setInside(c);
                map.getCoordinate(auxx, auxy).setInside(null);
                //saves the new coordinate and figure to the character
                c.setCoordinate(new Coordinate(x, y, map.getCoordinate(x, y).getObstacle(), c));
                //add movement
                MovementsModel movement = new MovementsModel();
                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                movement.setType("MoveCharacter");
                movement.setId(c.getId());
                Coordinate caux = new Coordinate(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getCoordinate().getObstacle(), null);
                movement.setCfinal(caux);
                game.getMovementQueue().add(movement);
            }

            isSomethingMoving = false;
            condition1.signalAll();
        } finally {
            monitor1.unlock();
        }
    }

    /**
     * Algorithm to move the given LargeAttack.
     *
     * @param a
     * @throws InterruptedException
     * @throws IOException
     */
    public void moveLargeAttack(LargeAttack a) throws InterruptedException, IOException {
        monitor1.lock();
        try {
            while (isSomethingMoving) {
                condition1.await();
            }
            isSomethingMoving = true;

            Coordinate coordinate = a.getCoordinate();
            int direction = a.getDirection();

            int x = coordinate.getX();
            int y = coordinate.getY();
            int auxx = x;
            int auxy = y;

            if (direction == Comms.up) {
                y = y - 1;
            } else if (direction == Comms.right) {
                x = x + 1;
            } else if (direction == Comms.down) {
                y = y + 1;
            } else if (direction == Comms.left) {
                x = x - 1;
            }

            if (a.getIsNew()) {
                if (!outOfBounds(x, y) && !a.hasCollision(x, y)) {
                    map.getCoordinate(x, y).setInside(a);
                    Coordinate coord = new Coordinate(x, y, map.getCoordinate(x, y).getObstacle(), a);
                    a.setCoordinate(coord);
                    a.setIsNew(false);
                    //add movement
                    MovementsModel movement = new MovementsModel();
                    movement.setTime(System.currentTimeMillis() - game.getStartTime());
                    movement.setType("CreateNewLargeAttack");
                    movement.setId(a.getCharacter().getId());
                    movement.setId2(a.getId());
                    Coordinate caux = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
                    movement.setCfinal(caux);
                    game.getMovementQueue().add(movement);
                } else {
                    a.setAttackCollision(true);
                    game.setLargeAttackOff(a.getId());
                    //
                    if (!outOfBounds(x, y)) {
                        if (map.getCoordinate(x, y).getInside() != null) {
                            if (map.getCoordinate(x, y).getInside().getClass().getSuperclass().getSimpleName().equals("OurCharacter")) {
                                OurCharacter c = (OurCharacter) map.getCoordinate(x, y).getInside();
                                c.receiveLargeAttack(a);
                                a.getCharacter().increaseInflingedShoots();
                                //add movement
                                MovementsModel movement = new MovementsModel();
                                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                movement.setType("NewCharacterLife");
                                movement.setId(c.getId());
                                movement.setLife(c.getLife());
                                game.getMovementQueue().add(movement);
                                if (c.getLife() == 0) {
                                    a.getCharacter().increaseKills();
                                    c.setAlive(false);
                                    map.getCoordinate(x, y).setInside(null);
                                    map.getCoordinate(x, y).setObstacle(Comms.numObstacles);
                                    movement = new MovementsModel();
                                    movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                    movement.setType("CharacterDeath");
                                    movement.setId(c.getId());
                                    Coordinate caux = new Coordinate(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getCoordinate().getObstacle(), null);
                                    movement.setCfinal(caux);
                                    game.getMovementQueue().add(movement);

                                    game.setNumCharactersAlive(game.getNumCharactersAlive() - 1);
                                    if (game.getNumCharactersAlive() == 1) {
                                        game.setEnded(true);
                                        movement = new MovementsModel();
                                        movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                        movement.setType("GameEnded");
                                        game.getMovementQueue().add(movement);
                                    }
                                }
                            } else if (map.getCoordinate(x, y).getInside().getClass().getSuperclass().getSimpleName().equals("LargeAttack")) {
                                LargeAttack a2 = (LargeAttack) map.getCoordinate(x, y).getInside();
                                a2.setAttackCollision(true);
                                game.setLargeAttackOff(a.getId());
                                //add movement
                                MovementsModel movement = new MovementsModel();
                                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                movement.setType("EndLargeAttack");
                                movement.setId(a2.getId());
                                game.getMovementQueue().add(movement);
                                map.getCoordinate(x, y).setInside(null);
                            }
                        }
                    }
                }
            } else if (!outOfBounds(x, y) && !a.hasCollision(x, y)) {
                map.getCoordinate(x, y).setInside(a);
                map.getCoordinate(auxx, auxy).setInside(null);
                Coordinate coord = new Coordinate(x, y, map.getCoordinate(x, y).getObstacle(), a);
                a.setCoordinate(coord);
                //add movement
                MovementsModel movement = new MovementsModel();
                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                movement.setType("MoveLargeAttack");
                movement.setId(a.getId());
                Coordinate caux = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
                movement.setCfinal(caux);
                game.getMovementQueue().add(movement);
            } else {
                game.setLargeAttackOff(a.getId());
                //add movement
                MovementsModel movement = new MovementsModel();
                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                movement.setType("EndLargeAttack");
                movement.setId(a.getId());
                game.getMovementQueue().add(movement);
                a.setAttackCollision(true);
                map.getCoordinate(auxx, auxy).setInside(null);
                //
                if (!outOfBounds(x, y)) {
                    if (map.getCoordinate(x, y).getInside() != null) {
                        if (map.getCoordinate(x, y).getInside().getClass().getSuperclass().getSimpleName().equals("OurCharacter")) {
                            OurCharacter c = (OurCharacter) map.getCoordinate(x, y).getInside();
                            c.receiveLargeAttack(a);
                            a.getCharacter().increaseInflingedShoots();
                            //add movement
                            movement = new MovementsModel();
                            movement.setTime(System.currentTimeMillis() - game.getStartTime());
                            movement.setType("NewCharacterLife");
                            movement.setId(c.getId());
                            movement.setLife(c.getLife());
                            game.getMovementQueue().add(movement);
                            if (c.getLife() == 0) {
                                a.getCharacter().increaseKills();
                                c.setAlive(false);
                                map.getCoordinate(x, y).setInside(null);
                                map.getCoordinate(x, y).setObstacle(Comms.numObstacles);

                                movement = new MovementsModel();
                                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                movement.setType("CharacterDeath");
                                movement.setId(c.getId());
                                Coordinate caux = new Coordinate(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getCoordinate().getObstacle(), null);
                                movement.setCfinal(caux);
                                game.getMovementQueue().add(movement);
                                game.setNumCharactersAlive(game.getNumCharactersAlive() - 1);
                                if (game.getNumCharactersAlive() == 1) {
                                    game.setEnded(true);
                                    movement = new MovementsModel();
                                    movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                    movement.setType("GameEnded");
                                    game.getMovementQueue().add(movement);
                                }
                            }
                        } else if (map.getCoordinate(x, y).getInside().getClass().getSuperclass().getSimpleName().equals("LargeAttack")) {
                            LargeAttack a2 = (LargeAttack) map.getCoordinate(x, y).getInside();
                            a2.setAttackCollision(true);
                            game.setLargeAttackOff(a.getId());
                            //add movement
                            movement = new MovementsModel();
                            movement.setTime(System.currentTimeMillis() - game.getStartTime());
                            movement.setType("EndLargeAttack");
                            movement.setId(a2.getId());
                            game.getMovementQueue().add(movement);
                            map.getCoordinate(x, y).setInside(null);
                        }
                    }
                }
            }

            isSomethingMoving = false;
            condition1.signalAll();
        } finally {
            monitor1.unlock();
        }
    }

    /**
     * Algorithm to move the given ShortAttack.
     *
     * @param a
     * @throws InterruptedException
     */
    public void moveShortAttack(ShortAttack a) throws InterruptedException {
        monitor1.lock();
        try {
            while (isSomethingMoving) {
                condition1.await();
            }
            isSomethingMoving = true;

            if (a.getIsNew()) {
                Coordinate coordinate = a.getCoordinate();
                int direction = a.getCharacter().getDirection();

                int x = coordinate.getX();
                int y = coordinate.getY();

                if (direction == Comms.up) {
                    y = y - 1;
                } else if (direction == Comms.right) {
                    x = x + 1;
                } else if (direction == Comms.down) {
                    y = y + 1;
                } else if (direction == Comms.left) {
                    x = x - 1;
                }

                if (!outOfBounds(x, y) && !a.hasCollision(x, y)) {
                    map.getCoordinate(x, y).setInside2(a);
                    Coordinate coord = map.getCoordinate(x, y);
                    a.setCoordinate(coord);
                    a.setIsNew(false);
                    //add movement
                    MovementsModel movement = new MovementsModel();
                    movement.setTime(System.currentTimeMillis() - game.getStartTime());
                    movement.setType("CreateNewShortAttack");
                    movement.setId(a.getCharacter().getId());
                    movement.setId2(a.getId());
                    Coordinate caux = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
                    movement.setCfinal(caux);
                    game.getMovementQueue().add(movement);

                    //collision with a character
                    if (coord.getInside() != null) {
                        if (coord.getInside().getClass().getSuperclass().getSimpleName().equals("OurCharacter")) {
                            OurCharacter c = (OurCharacter) map.getCoordinate(x, y).getInside();
                            if (a.getCharacter().getId() != c.getId()) {
                                c.receiveShortAttack(a);
                                a.getCharacter().increaseInflingedShoots();
                                //add movement
                                movement = new MovementsModel();
                                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                movement.setType("NewCharacterLife");
                                movement.setId(c.getId());
                                movement.setLife(c.getLife());
                                game.getMovementQueue().add(movement);
                                if (c.getLife() == 0) {
                                    a.getCharacter().increaseKills();
                                    c.setAlive(false);
                                    map.getCoordinate(x, y).setInside(null);
                                    map.getCoordinate(x, y).setObstacle(Comms.numObstacles);
                                    movement = new MovementsModel();
                                    movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                    movement.setType("CharacterDeath");
                                    movement.setId(c.getId());
                                    caux = new Coordinate(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getCoordinate().getObstacle(), null);
                                    movement.setCfinal(caux);
                                    game.getMovementQueue().add(movement);
                                    game.setNumCharactersAlive(game.getNumCharactersAlive() - 1);
                                    if (game.getNumCharactersAlive() == 1) {
                                        game.setEnded(true);
                                        movement = new MovementsModel();
                                        movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                        movement.setType("GameEnded");
                                        game.getMovementQueue().add(movement);
                                    }
                                }
                            }
                        }
                    }

                    //increase loop variable and check end of short attack
                    a.setCurrentLoop(a.getCurrentLoop() + 1);
                    if (a.getCurrentLoop() == a.getLoopsActive()) {
                        a.setEnded(true);
                        coord = a.getCoordinate();
                        map.getCoordinate(coord.getX(), coord.getY()).setInside2(null);

                        movement = new MovementsModel();
                        movement.setTime(System.currentTimeMillis() - game.getStartTime());
                        movement.setType("EndShortAttack");
                        movement.setId(a.getId());
                        caux = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
                        movement.setCfinal(caux);
                        game.getMovementQueue().add(movement);
                    }
                } else {
                    a.setEnded(true);
                }
            } else {
                Coordinate coord = map.getCoordinate(a.getCoordinate().getX(), a.getCoordinate().getY());
                //collision with a character
                if (coord.getInside() != null) {
                    if (coord.getInside().getClass().getSuperclass().getSimpleName().equals("OurCharacter")) {
                        OurCharacter c = (OurCharacter) coord.getInside();
                        if (a.getCharacter().getId() != c.getId()) {
                            c.receiveShortAttack(a);
                            a.getCharacter().increaseInflingedShoots();
                            //add movement
                            MovementsModel movement = new MovementsModel();
                            movement.setTime(System.currentTimeMillis() - game.getStartTime());
                            movement.setType("NewCharacterLife");
                            movement.setId(c.getId());
                            movement.setLife(c.getLife());
                            game.getMovementQueue().add(movement);
                            if (c.getLife() == 0) {
                                a.getCharacter().increaseKills();
                                c.setAlive(false);
                                coord.setInside(null);
                                coord.setObstacle(Comms.numObstacles);
                                movement = new MovementsModel();
                                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                movement.setType("CharacterDeath");
                                movement.setId(c.getId());
                                Coordinate caux = new Coordinate(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getCoordinate().getObstacle(), null);
                                movement.setCfinal(caux);
                                game.getMovementQueue().add(movement);

                                game.setNumCharactersAlive(game.getNumCharactersAlive() - 1);
                                if (game.getNumCharactersAlive() == 1) {
                                    game.setEnded(true);
                                    movement = new MovementsModel();
                                    movement.setTime(System.currentTimeMillis() - game.getStartTime());
                                    movement.setType("GameEnded");
                                    game.getMovementQueue().add(movement);
                                }
                            }
                        }
                    }
                }

                //increase loop variable and check end of short attack
                a.setCurrentLoop(a.getCurrentLoop() + 1);
                if (a.getCurrentLoop() == a.getLoopsActive()) {
                    a.setEnded(true);
                    map.getCoordinate(coord.getX(), coord.getY()).setInside2(null);

                    MovementsModel movement = new MovementsModel();
                    movement.setTime(System.currentTimeMillis() - game.getStartTime());
                    movement.setType("EndShortAttack");
                    movement.setId(a.getId());
                    Coordinate caux = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
                    movement.setCfinal(caux);
                    game.getMovementQueue().add(movement);
                }
            }

            isSomethingMoving = false;
            condition1.signalAll();
        } finally {
            monitor1.unlock();
        }
    }

    /**
     * Kills the character.
     *
     * @param id
     * @throws InterruptedException
     */
    public void kill(int id) throws InterruptedException {
        monitor1.lock();
        try {
            while (isSomethingMoving) {
                condition1.await();
            }
            isSomethingMoving = true;

            OurCharacter c = game.getCharacter(id);
            c.setLife(0);
            MovementsModel movement = new MovementsModel();
            movement.setTime(System.currentTimeMillis() - game.getStartTime());
            movement.setType("NewCharacterLife");
            movement.setId(c.getId());
            movement.setLife(c.getLife());
            game.getMovementQueue().add(movement);

            c.setAlive(false);
            map.getCoordinate(c.getCoordinate().getX(), c.getCoordinate().getY()).setInside(null);
            map.getCoordinate(c.getCoordinate().getX(), c.getCoordinate().getY()).setObstacle(Comms.numObstacles);
            movement = new MovementsModel();
            movement.setTime(System.currentTimeMillis() - game.getStartTime());
            movement.setType("CharacterDeath");
            movement.setId(c.getId());
            Coordinate caux = new Coordinate(c.getCoordinate().getX(), c.getCoordinate().getY(), c.getCoordinate().getObstacle(), null);
            movement.setCfinal(caux);
            game.getMovementQueue().add(movement);

            game.setNumCharactersAlive(game.getNumCharactersAlive() - 1);
            if (game.getNumCharactersAlive() == 1) {
                game.setEnded(true);
                movement = new MovementsModel();
                movement.setTime(System.currentTimeMillis() - game.getStartTime());
                movement.setType("GameEnded");
                game.getMovementQueue().add(movement);
            }

            isSomethingMoving = false;
            condition1.signalAll();
        } finally {
            monitor1.unlock();
        }
    }

    /**
     * Executes the given LargeAttack.
     *
     * @param a
     */
    public void executeLargeAttack(final LargeAttack a) {
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!a.getAttackCollision() && !game.isEnded()) {
                    try {
                        moveLargeAttack(a);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, a.getCharacter().getDelayLargeAttackSpeedMovement());
    }

    /**
     * Executes the given ShortAttack.
     *
     * @param a
     */
    public void executeShortAttack(final ShortAttack a) {
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!a.isEnded() && !game.isEnded()) {
                    try {
                        moveShortAttack(a);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, Comms.delayShowShortAttack);
    }

    /**
     * Checks if a position is inside the map.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean outOfBounds(int x, int y) {
        return !(x >= 0 && x < Comms.obstacleColumns && y >= 0 && y < Comms.obstacleRows);
    }

}
