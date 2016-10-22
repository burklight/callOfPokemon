package server.controllers;

import server.database.DataBase;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import utilities.map.OurMap;
import utilities.communications.Comms;

/**
 * This class controls the accesses to the database (mutual exclusion).
 */
public class DataBaseController {

    private final ReentrantLock mon;
    private final Condition con;
    private final DataBase db;
    private boolean accessing;

    public DataBaseController(DataBase d) {
        accessing = false;
        db = d;
        mon = new ReentrantLock();
        con = mon.newCondition();
    }

    /**
     * Adds movements to the DataBase.
     *
     * @param idGame
     * @param movs
     * @return
     * @throws Exception
     */
    public int addMovements(int idGame, ArrayList movs) throws Exception {
        mon.lock();
        int id = -1;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            id = db.addNewMovements(movs, idGame);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return id;
    }

    /**
     * Adds a player.
     *
     * @param usersName
     * @param i
     * @param charactersString
     * @param idGame
     * @throws Exception
     */
    public void addPlayer(String usersName, int i, String charactersString, int idGame) throws Exception {
        mon.lock();
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            db.addNewPlayer(usersName, i, charactersString, idGame);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
    }

    /**
     * Adds the map.
     *
     * @param map
     * @return
     * @throws Exception
     */
    public int addMap(OurMap map) throws Exception {
        mon.lock();
        int idMap = 0;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            idMap = db.addNewMap(map);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return idMap;
    }

    /**
     * Adds the game.
     *
     * @param idMap
     * @return
     * @throws Exception
     */
    public int addGame(int idMap) throws Exception {
        mon.lock();
        int id = 0;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            id = db.addNewGame(idMap);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return id;
    }

    /**
     * Adds the score.
     *
     * @param l
     * @param position
     * @param i
     * @param r
     * @param kills
     * @param usersName
     * @param idGame
     * @throws Exception
     */
    public void addScore(long l, int position, int i, int r, int kills, String usersName, int idGame) throws Exception {
        mon.lock();
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            db.addNewScore(l, position, i, r, kills, usersName, idGame);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
    }

    /**
     * Gets the usable characters by that user.
     *
     * @param user
     * @return usable characters.
     * @throws Exception
     */
    public ArrayList getUsableCharacters(String user) throws Exception {
        mon.lock();
        ArrayList u = new ArrayList<>();
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            u = db.getUsableCharacters(user);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return u;
    }

    /**
     * Gets the scoreboard.
     *
     * @return
     * @throws Exception
     */
    public String[] getScoreBoard() throws Exception {
        mon.lock();
        String s[] = null;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            s = db.getScoreBoard(Comms.numScoresGeneral);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return s;
    }

    /**
     * Gets the user's scores.
     *
     * @param name
     * @return
     * @throws Exception
     */
    public String[] getUserScores(String name) throws Exception {
        mon.lock();
        String s[] = null;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            s = db.getUserScores(name, Comms.numScoresUser);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return s;
    }

    /**
     * Gets idGamePlayer.
     *
     * @param user
     * @return idGamePlayer.
     * @throws Exception
     */
    public int getIdGamePlayed(String user) throws Exception {
        mon.lock();
        int id = 0;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            id = db.getIdGamePlayed(user);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return id;
    }

    /**
     * Gets the record of an specified game.
     *
     * @param gameId
     * @return record.
     * @throws Exception
     */
    public ArrayList getRecord(int gameId) throws Exception {
        mon.lock();
        ArrayList r = new ArrayList<>();
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            r = db.getRecord(gameId);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return r;
    }

    /**
     * Gets the map of an specified game.
     *
     * @param gameId
     * @return map.
     * @throws Exception
     */
    public OurMap getMap(int gameId) throws Exception {
        mon.lock();
        OurMap m = null;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            m = db.getMap(gameId);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return m;
    }

    /**
     * Gets the names of the character of an specified game.
     *
     * @param gameId
     * @return a, an ArrayList with the names.
     * @throws Exception
     */
    public ArrayList getCharacterNames(int gameId) throws Exception {
        mon.lock();
        ArrayList a = new ArrayList<>();
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            a = db.getCharacterNames(gameId);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return a;
    }

    /**
     * Gets the scores of an specified game.
     *
     * @param idGame
     * @return scores.
     * @throws Exception
     */
    public String[] getGameScores(int idGame) throws Exception {
        mon.lock();
        String s[] = null;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            s = db.getGameScores(idGame);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return s;
    }

    /**
     * Sets user record.
     *
     * @param idGame
     * @param usersName
     * @throws Exception
     */
    public void setUserRecord(int idGame, String usersName) throws Exception {
        mon.lock();
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            db.setUserRecord(idGame, usersName);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
    }

    /**
     * Removes old record.
     *
     * @param user
     * @throws Exception
     */
    public void removeRecord(String user) throws Exception {
        mon.lock();
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            db.removeRecord(user);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
    }

    /**
     * Registers the user.
     *
     * @param user
     * @param pass
     * @return id (-1 if there's already a user with the same name).
     * @throws Exception
     */
    public int registerUser(String user, String pass) throws Exception {
        mon.lock();
        int id = 0;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            id = db.registerUser(user, pass);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return id;
    }

    /**
     * Search of an existing user.
     *
     * @param user
     * @param pass
     * @return its id (-1 if it doesn't exist).
     * @throws Exception
     */
    public int loginUser(String user, String pass) throws Exception {
        mon.lock();
        int id = 0;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            id = db.loginUser(user, pass);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return id;
    }

    /**
     * Changes the password of the given user.
     *
     * @param user
     * @param pass
     * @return true if possible, false otherwise.
     * @throws Exception
     */
    public boolean changePassword(String user, String pass) throws Exception {
        mon.lock();
        boolean b = false;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            b = db.changePassword(user, pass);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return b;
    }

    /**
     * Removes the coordinates of the given game.
     *
     * @param idGame
     * @return The id of the game removed.
     * @throws Exception
     */
    public int removeCoordinates(int idGame) throws Exception {
        mon.lock();
        int id = 0;
        try {
            while (accessing) {
                con.await();
            }
            accessing = true;
            id = db.removeCoordinates(idGame);
            accessing = false;
            con.signalAll();
        } finally {
            mon.unlock();
        }
        return id;
    }

}
