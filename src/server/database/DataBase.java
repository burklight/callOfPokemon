package server.database;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import utilities.map.OurMap;
import player.models.MovementsModel;
import utilities.communications.Comms;
import utilities.graphics.Coordinate;

/**
 * This class is used to connect and perform operations into the database.
 */
public class DataBase {

    private final String dbName = "callofpokemondb";
    private Connection con;

    public DataBase() {
        try {
            con = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Acess the database, needs to be called first to use any method.
     *
     * @return the connection with it
     * @throws Exception
     */
    public Connection getConnection() throws Exception {
        if (con == null) {
            con = DriverManager.getConnection("jdbc:ucanaccess://" + dbName + ".mdb");
            System.out.println("Connected to database.");
        } else {
            System.out.println("Already connected to the database.");
        }
        return con;
    }

    /**
     * Ends the connection with the base.
     *
     * @throws Exception
     */
    public void close() throws Exception {
        if (con == null) {
            System.out.println("Not connected to database.");
        } else {
            con.close();
            con = null;
            System.out.println("Disconnected from database.");
        }
    }

    /**
     * Registers a new user in the database. (if there is already a user with
     * the given name returns -1)
     *
     * @param user
     * @param pass
     * @return key of the new user created or -1
     * @throws Exception
     */
    public int registerUser(String user, String pass) throws Exception {
        PreparedStatement stmt = null;
        Statement st = null;
        int key = 0;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet r = st.executeQuery("SELECT * FROM User");
            while (r.next()) {
                if (r.getString("UserName").equals(user)) {
                    key = -1;
                }
            }
            r.close();
            if (key >= 0) {
                stmt = con.prepareStatement("INSERT into User (UserName, Password) VALUES (?, ?)");
                System.out.println("Ready to register a new user.");
                stmt.setString(1, user);
                stmt.setString(2, pass);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                key = rs.getInt(1);
                rs.close();
            }
        } finally {
            if (stmt != null && st != null) {
                stmt.close();
                st.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Search for a existing user.
     *
     * @param name
     * @param pass
     * @return its idUser or -1 if not found
     * @throws Exception
     */
    public int loginUser(String name, String pass) throws Exception {
        Statement stmt = null;
        int key = -1;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet r = stmt.executeQuery("SELECT * FROM User");
            while (r.next()) {
                if (r.getString("UserName").equals(name) && r.getString("Password").equals(pass)) {
                    System.out.println("Found existing user.");
                    key = r.getInt("IdUser");
                }
            }
            r.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Gets the usable unlocked characters by a user.
     *
     * @param name
     * @return
     * @throws Exception
     */
    public ArrayList getUsableCharacters(String name) throws Exception {
        ArrayList usables = new ArrayList<>();
        usables.addAll(Arrays.asList(Comms.characters));
        if (!name.equals("admin")) {
            if (!requestCharacter(null, name, 1000000)) {
                usables.remove("Haruhi");
            }
            if (!requestCharacter(null, name, 500000)) {
                usables.remove("Accelerator");
                usables.remove("Negi");
            }
            if (!requestCharacter(null, name, 300000)) {
                usables.remove("Nanoha");
                usables.remove("Zero");
            }
            if (!requestCharacter(null, name, 200000)) {
                usables.remove("Link");
            }
            if (!requestCharacter("Charmander", name, 150000)) {
                usables.remove("Charizard");
            }
            if (!requestCharacter("CharmanderShiny", name, 150000)) {
                usables.remove("CharizardShiny");
            }
            if (!requestCharacter("Pikachu", name, 150000)) {
                usables.remove("Raichu");
            }
            if (!requestCharacter("PikachuShiny", name, 150000)) {
                usables.remove("RaichuShiny");
            }
            if (!requestCharacter("Squirtle", name, 150000)) {
                usables.remove("Blastoise");
            }
            if (!requestCharacter("SquirtleShiny", name, 150000)) {
                usables.remove("BlastoiseShiny");
            }
            if (!requestCharacter("Bulbasur", name, 150000)) {
                usables.remove("Venusaur");
            }
            if (!requestCharacter("BulbasurShiny", name, 150000)) {
                usables.remove("VenusaurShiny");
            }
        }
        return usables;
    }

    /**
     * Calculates if the total scores a a given character is higher than the one
     * given. If character is null, looks for the total score of all characters.
     *
     * @param character
     * @param name
     * @param score
     * @return true if the charcter is usable
     * @throws Exception
     */
    private boolean requestCharacter(String character, String name, int score) throws Exception {
        Statement stmt = null;
        String c = null;
        int punt = 0;
        boolean ok = false;
        String query = "select UserName, Character, Score "
                + "from ScoreData, Player, User "
                + "where Player.IdScore = ScoreData.IdScore and "
                + "User.IdUser = Player.IdUser ";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (character == null) {
                while (rs.next()) {
                    String n = rs.getString("UserName");
                    if (name.equals(n)) {
                        punt += rs.getInt("Score");
                    }
                }
            } else {
                while (rs.next()) {
                    c = rs.getString("Character");
                    String n = rs.getString("UserName");
                    if (name.equals(n) && character.equals(c)) {
                        punt += rs.getInt("Score");
                    }
                }
            }
            ok = (score <= punt);
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return ok;
    }

    /**
     * Changes the password of a given user if possible.
     *
     * @param name
     * @param newPass
     * @return true to signal a satisfactory operation, false otherwise
     * @throws Exception
     */
    public boolean changePassword(String name, String newPass) throws Exception {
        Statement stmt = null;
        boolean done = false;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet r = stmt.executeQuery("SELECT * FROM User");
            while (r.next()) {
                if (r.getString("UserName").equals(name)) {
                    r.updateString("Password", newPass);
                    r.updateRow();
                    done = true;
                    System.out.println("The new password has been saved.");
                }
            }
            r.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return done;
    }

    /**
     * Gets the top 'x' scores of a given user.
     *
     * @param user
     * @param x
     * @return information string of the scores
     * @throws Exception
     */
    public String[] getUserScores(String user, int x) throws Exception {
        String s[] = new String[x];
        Statement stmt = null;
        String query = "select UserName, Character, GameDate, Score "
                + "from ScoreData, Game, Player, User "
                + "where Player.IdScore = ScoreData.IdScore and "
                + "Game.IdGame = Player.IdGame and User.IdUser = Player.IdUser "
                + "order by Score desc";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Ready to get the user scores.");
            int i = 0;
            while (rs.next() && i < x) {
                String name = rs.getString("UserName");
                if (name.equals(user)) {
                    String character = rs.getString("Character");
                    Date date = rs.getDate("GameDate");
                    int punt = rs.getInt("Score");
                    i++;
                    s[i - 1] = i + ". " + name + ", " + character + ", " + date + ", " + punt;
                }
            }
            while (x > i) {
                s[i] = "There aren't scores...";
                i++;
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return s;
    }

    /**
     * Gets the top "x" scores data.
     *
     * @param x
     * @return string vector with the top 3 scores from top to bottom
     * @throws Exception
     */
    public String[] getScoreBoard(int x) throws Exception {
        String s[] = new String[x];
        Statement stmt = null;
        //because java
        String query = "select UserName, Character, GameDate, Score "
                + "from ScoreData, Game, Player, User "
                + "where Player.IdScore = ScoreData.IdScore and "
                + "Game.IdGame = Player.IdGame and User.IdUser = Player.IdUser "
                + "order by Score desc";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Ready to get the ScoreBoard.");
            for (int i = 1; i <= x; i++) {
                if (rs.next()) {
                    String name = rs.getString("UserName");
                    String character = rs.getString("Character");
                    Date date = rs.getDate("GameDate");
                    int punt = rs.getInt("Score");
                    s[i - 1] = i + ". " + name + ", " + character + ", " + date + ", " + punt;
                } else {
                    s[i - 1] = "There aren't scores...";
                }
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return s;
    }

    /**
     * Creates new map in the database.
     *
     * @param m
     * @return the identificator of the new map created
     * @throws java.lang.Exception
     */
    public int addNewMap(OurMap m) throws Exception {
        PreparedStatement stmt = null;
        int key = 0;
        //because java again
        try {
            stmt = con.prepareStatement("INSERT into Map (MapType, Rows, Columns) VALUES (?, ?, ?)");
            System.out.println("Ready to add a new Map.");
            stmt.setInt(1, m.getType());
            stmt.setInt(2, Comms.obstacleRows);
            stmt.setInt(3, Comms.obstacleColumns);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            key = rs.getInt(1);
            rs.close();
            addNewCoordinates(m.getCoordinates(), key);
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Adds the coordinates to the map with the given identification.
     *
     * @param c
     * @param mapId
     * @return identificator of the new coordinate
     * @throws java.lang.Exception
     */
    private void addNewCoordinates(ArrayList<Coordinate> cord, int mapId) throws Exception {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT into Coordinate (x, y, Obstacle, IdMap) VALUES (?, ?, ?, ?)");
            System.out.println("Ready to add coordinates.");
            for (Coordinate c : cord) {
                stmt.setInt(1, c.getX());
                stmt.setInt(2, c.getY());
                stmt.setInt(3, c.getObstacle());
                stmt.setInt(4, mapId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
    }

    /**
     * Adds all the following movements to the base.
     *
     * @param movs
     * @param idGame
     * @return id of the map pertaining to the game or -1 if not found
     * @throws Exception
     */
    public int addNewMovements(ArrayList<MovementsModel> movs, int idGame) throws Exception {
        PreparedStatement stmt = null;
        Statement st = null;
        int key = -1;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Ready to record a new movement.");
            ResultSet rs = st.executeQuery("SELECT * FROM Game "
                    + "WHERE Game.IdGame = " + idGame);
            rs.next();
            key = rs.getInt("IdMap");
            rs.close();
            stmt = con.prepareStatement("INSERT into Movement "
                    + "(Id1, Id2, Direction, TimeInstant, Type, IdGame, IdCoordinateIni, IdCoordinateFin, Life) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (MovementsModel m : movs) {
                stmt.setInt(1, m.getId());
                stmt.setInt(2, m.getId2());
                stmt.setInt(3, m.getDirection());
                stmt.setInt(4, (int) m.getTime());
                stmt.setString(5, m.getType());
                stmt.setInt(6, idGame);
                stmt.setInt(7, getIdCoordinate(m.getCinitial(), key));
                stmt.setInt(8, getIdCoordinate(m.getCfinal(), key));
                stmt.setInt(9, m.getLife());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } finally {
            if (stmt != null && st != null) {
                stmt.close();
                st.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
            if (key == -1) {
                System.err.println("Map not found, game not saved.");
            }
        }
        return key;
    }

    /**
     * Gets the id of a given coordinate in a given idMap. (if the map has been
     * created the coordinate must exist and it must be unique)
     *
     * @param c
     * @param idMap
     * @return id of the coordinate
     * @throws Exception
     */
    public int getIdCoordinate(Coordinate c, int idMap) throws Exception {
        Statement stmt = null;
        int key = -1;
        try {
            if (c != null) {
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery("SELECT * FROM Coordinate "
                        + "WHERE Coordinate.x = " + c.getX() + " AND "
                        + "Coordinate.y = " + c.getY() + " AND "
                        + "Coordinate.IdMap = " + idMap);
                rs.next();
                key = rs.getInt("IdCoordinate");
                rs.close();
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Creates new game to the database with the given parameters.
     *
     * @param mapId
     * @return identificator of the new map
     * @throws Exception
     */
    public int addNewGame(int mapId) throws Exception {
        PreparedStatement stmt = null;
        int key = 0;
        try {
            stmt = con.prepareStatement("INSERT into Game (GameDate, Players, IdMap) VALUES (?, ?, ?)");
            //gets the current date in a tractable format
            DateFormat year = new SimpleDateFormat("yyyy");
            DateFormat month = new SimpleDateFormat("MM");
            DateFormat day = new SimpleDateFormat("dd");
            Calendar cal = Calendar.getInstance();
            int y = Integer.parseInt(year.format(cal.getTime())) - 1900;
            int m = Integer.parseInt(month.format(cal.getTime())) - 1;
            int d = Integer.parseInt(day.format(cal.getTime()));
            //new values of new Game
            System.out.println("Ready to create a game.");
            stmt.setDate(1, new Date(y, m, d), cal);
            stmt.setInt(2, Comms.NUM_PLAYERS);
            stmt.setInt(3, mapId);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            key = rs.getInt(1);
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Creates a new player in the database. (without scoredata)
     *
     * @param userName name of the user
     * @param numPlayer
     * @param character name of the character
     * @param gameId
     * @return identificator of the new player
     * @throws Exception
     */
    public int addNewPlayer(String userName, int numPlayer, String character, int gameId) throws Exception {
        PreparedStatement stmt = null;
        Statement st = null;
        int key = -1;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT * FROM User");
            while (rs.next()) {
                if (rs.getString("UserName").equals(userName)) {
                    key = rs.getInt("IdUser");
                    break;
                }
            }
            rs.close();
            stmt = con.prepareStatement("INSERT into Player (IdUser, NumPlayer, Character, IdGame) VALUES (?, ?, ?, ?)");
            System.out.println("Ready to add a player.");
            stmt.setInt(1, key);
            stmt.setInt(2, numPlayer);
            stmt.setString(3, character);
            stmt.setInt(4, gameId);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            rs.next();
            key = rs.getInt(1);
            rs.close();
        } finally {
            if (stmt != null && st != null) {
                stmt.close();
                st.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Creates the new score in the database of a specific player.
     *
     * @param scoreTime
     * @param position
     * @param iShoots
     * @param rShoots
     * @param user
     * @param kills
     * @param idGame
     * @return identificator of the new Score
     * @throws Exception
     */
    public int addNewScore(long scoreTime, int position, int iShoots, int rShoots, int kills, String user, int idGame) throws Exception {
        PreparedStatement stmt = null;
        Statement st = null;
        int key = -1;
        int idUser = -1;
        int playerId = -1;
        try {
            stmt = con.prepareStatement("INSERT into ScoreData "
                    + "(ScoreTime, Position, InflingedShoots, ReceivedShoots, Kills, Score) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");
            System.out.println("Ready to create a score and assign it.");
            stmt.setInt(1, (int) scoreTime);
            stmt.setInt(2, Comms.NUM_PLAYERS - position);
            stmt.setInt(3, iShoots);
            stmt.setInt(4, rShoots);
            stmt.setInt(5, kills);
            stmt.setInt(6, calculateScore(scoreTime, iShoots, rShoots, kills, position));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            key = rs.getInt(1);
            rs.close();
            //assign the score to the player
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT * FROM User");
            while (rs.next()) {
                if (rs.getString("UserName").equals(user)) {
                    idUser = rs.getInt("IdUser");
                    break;
                }
            }
            rs.close();
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT * FROM Player");
            while (rs.next()) {
                if (rs.getInt("IdUser") == idUser && rs.getInt("IdGame") == idGame) {
                    playerId = rs.getInt("IdPlayer");
                    break;
                }
            }
            rs.close();
            stmt = con.prepareStatement("update Player set IdScore= ? where Player.IdPlayer = " + playerId);
            stmt.setInt(1, key);
            stmt.executeUpdate();
        } finally {
            if (stmt != null && st != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Algorithm to calculate the score.
     *
     * @param scoreTime
     * @param iShoots
     * @param rShoots
     * @param kills
     * @param position
     * @return
     */
    private int calculateScore(long scoreTime, int iShoots, int rShoots, int kills, int position) {
        int score = 1000 * (iShoots - rShoots);
        if (score < 0) {
            score = 0;
        }
        score += kills * 5000 + position * Comms.NUM_PLAYERS * 1000;
        if (position == Comms.NUM_PLAYERS - 1) {
            int i = 15000 * Comms.NUM_PLAYERS - ((int) scoreTime);
            if (i >= 0) {
                score += i;
            }
        }
        return score;
    }

    /**
     * Assigns the saved game to a player.
     *
     * @param idGame
     * @param user
     * @throws Exception
     */
    public void setUserRecord(int idGame, String user) throws Exception {
        Statement st = null;
        PreparedStatement stmt = null;
        int idUser = -1;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT * FROM User");
            while (rs.next()) {
                if (rs.getString("UserName").equals(user)) {
                    idUser = rs.getInt("IdUser");
                    break;
                }
            }
            rs.close();
            stmt = con.prepareStatement("update Movement set IdUser= ? where Movement.IdGame = " + idGame);
            stmt.setInt(1, idUser);
            stmt.executeUpdate();
        } finally {
            if (stmt != null && st != null) {
                stmt.close();
                st.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
    }

    /**
     * Deletes the recorded game specified by the user name.
     *
     * @param user
     * @throws Exception
     */
    public void removeRecord(String user) throws Exception {
        Statement st = null;
        PreparedStatement stmt = null;
        int idUser = -1;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT * FROM User");
            while (rs.next()) {
                if (rs.getString("UserName").equals(user)) {
                    idUser = rs.getInt("IdUser");
                    break;
                }
            }
            rs.close();
            stmt = con.prepareStatement("DELETE FROM Movement WHERE IdUser = " + idUser);
            System.out.println("Ready to delete the record.");
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
    }

    /**
     * Removes the coordinates of the map of a given game.
     *
     * @param idGame
     * @return The id of the map "removed"
     * @throws Exception
     */
    public int removeCoordinates(int idGame) throws Exception {
        Statement st = null;
        PreparedStatement stmt = null;
        int idMap = -1;
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT IdMap, IdGame FROM Game");
            while (rs.next()) {
                if (rs.getInt("IdGame") == idGame) {
                    idMap = rs.getInt("IdMap");
                    break;
                }
            }
            rs.close();
            stmt = con.prepareStatement("DELETE FROM Coordinate WHERE IdMap = " + idMap);
            System.out.println("Ready to delete the coordinates.");
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return idMap;
    }

    /**
     * Gets the idGame of the game saved from a user.
     *
     * @param user
     * @return id of the game
     * @throws Exception
     */
    public int getIdGamePlayed(String user) throws Exception {
        Statement stmt = null;
        int key = -1;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM User");
            while (rs.next()) {
                if (rs.getString("UserName").equals(user)) {
                    key = rs.getInt("IdUser");
                    break;
                }
            }
            rs.close();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT IdGame, IdUser FROM Movement "
                    + "WHERE Movement.IdUser = " + key);
            if (rs.next()) {
                key = rs.getInt("IdGame");
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return key;
    }

    /**
     * Gets a specified record of a game from a idGame.
     *
     * @param idGame
     * @return array of movements
     * @throws Exception
     */
    public ArrayList<MovementsModel> getRecord(int idGame) throws Exception {
        Statement stmt = null;
        ArrayList<MovementsModel> mov = new ArrayList<>();
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Ready to get the record.");
            ResultSet rs = stmt.executeQuery("SELECT * FROM Movement "
                    + "WHERE Movement.IdGame = " + idGame
                    + " order by TimeInstant asc");
            while (rs.next()) {
                int id1 = rs.getInt("Id1");
                int id2 = rs.getInt("Id2");
                int d = rs.getInt("Direction");
                long t = rs.getInt("TimeInstant");
                String type = rs.getString("Type");
                int l = rs.getInt("Life");
                Coordinate cini = getCoordinate(rs.getInt("IdCoordinateIni"));
                Coordinate cfin = getCoordinate(rs.getInt("IdCoordinateFin"));
                mov.add(new MovementsModel(type, id1, id2, d, l, t, cini, cfin));
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
            if (mov.isEmpty()) {
                System.out.println("This record doesn't exist.");
            }
        }
        return mov;
    }

    /**
     * Gets the character names which the players used in the given game. (the
     * names are given in order of the num player)
     *
     * @param idGame
     * @return array with the name
     */
    public ArrayList<String> getCharacterNames(int idGame) throws Exception {
        Statement stmt = null;
        ArrayList names = new ArrayList<>();
        int numPlayer = -1;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT IdGame, Character, NumPlayer FROM Player "
                    + "order by NumPlayer asc");
            while (rs.next()) {
                if (rs.getInt("IdGame") == idGame) {
                    numPlayer = rs.getInt("NumPlayer");
                    names.add(numPlayer, rs.getString("Character"));
                }
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return names;
    }

    /**
     * Gets the map used for a game.
     *
     * @param idGame
     * @return map
     * @throws Exception
     */
    public OurMap getMap(int idGame) throws Exception {
        Statement stmt = null;
        int key = -1;
        ArrayList<Coordinate> coord = new ArrayList<>();
        OurMap m = new OurMap();
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT IdMap, IdGame FROM Game Where Game.IdGame = " + idGame);
            if (rs.next()) {
                key = rs.getInt("IdMap");
            }
            rs.close();
            rs = stmt.executeQuery("SELECT MapType, IdMap FROM Map Where Map.IdMap = " + key);
            if (rs.next()) {
                m.setType(rs.getInt("MapType"));
            }
            rs.close();
            rs = stmt.executeQuery("SELECT x, y, Obstacle, IdMap FROM Coordinate Where Coordinate.IdMap = " + key);
            while (rs.next()) {
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int o = rs.getInt("Obstacle");
                coord.add(new Coordinate(x, y, o, null));
            }
            rs.close();
            m.setCoordinates(coord);
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return m;
    }

    /**
     * Gets the given coordinate on the database of coordinates.
     *
     * @param idCoord
     * @return coordinate if found
     * @throws Exception
     */
    private Coordinate getCoordinate(int idCoord) throws Exception {
        Statement stmt = null;
        Coordinate c = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM Coordinate "
                    + "WHERE Coordinate.IdCoordinate = " + idCoord);
            if (rs.next()) {
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int o = rs.getInt("Obstacle");
                c = new Coordinate(x, y, o, null);
            }
            rs.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return c;
    }

    /**
     * Gets the scores of a game.
     *
     * @param idGame
     * @return string with the scores information
     * @throws Exception
     */
    public String[] getGameScores(int idGame) throws Exception {
        Statement stmt = null;
        String s[] = new String[Comms.NUM_PLAYERS];
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT IdGame, UserName, Character, GameDate, Score FROM "
                    + "Player, ScoreData, Game, User "
                    + "WHERE Player.IdGame = Game.IdGame and Player.IdScore = ScoreData.IdScore "
                    + "and User.IdUser = Player.IdUser "
                    + "order by Score desc");
            System.out.println("Ready to get the scores of the game.");
            int i = 0;
            while (rs.next()) {
                if (rs.getInt("IdGame") == idGame) {
                    String name = rs.getString("UserName");
                    String character = rs.getString("Character");
                    Date date = rs.getDate("GameDate");
                    int punt = rs.getInt("Score");
                    i++;
                    s[i - 1] = i + ". " + name + ", " + character + ", " + date + ", " + punt;
                }
            }
            rs.close();
            while (i < Comms.NUM_PLAYERS) {
                s[i] = "Couldn't find this score...";
                i++;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
                System.out.println("Ended operations.");
            } else if (con == null) {
                System.out.println("Not connected to database.");
            }
        }
        return s;
    }
}
