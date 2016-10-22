package server.controllers;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import utilities.map.OurMap;
import player.models.MovementsModel;
import server.models.GameModel;
import utilities.communications.ServerNIO;
import utilities.communications.Comms;
import utilities.communications.DataPacket;
import utilities.graphics.Coordinate;

/**
 * This class controls the messages that has to send the server to the clients.
 */
public class SendController extends Thread {

    protected ServerNIO server;
    protected GameModel game;
    protected ReceiveController controllerReceive;
    protected DataBaseController dbc;
    private int position;
    private final ArrayList<MovementsModel> movs;

    public SendController(ServerNIO s, GameModel g) {
        server = s;
        game = g;
        dbc = server.getDbc();
        position = 0;
        movs = new ArrayList<>();
    }

    /**
     * Sets controllerReceive.
     *
     * @param controllerReceive
     */
    public void setControllerReceive(ReceiveController controllerReceive) {
        this.controllerReceive = controllerReceive;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MovementsModel m = new MovementsModel();
                m = game.getMovementQueue().take();
                switch (m.getType()) {
                    case "ChangeDirection":
                        sendChangeDirection(m.getId(), m.getDirection());
                        break;
                    case "MoveCharacter":
                        sendMoveCharacter(m.getId(), m.getCfinal());
                        break;
                    case "CreateNewLargeAttack":
                        sendCreateNewLargeAttack(m.getId(), m.getId2(), m.getCfinal());
                        break;
                    case "MoveLargeAttack":
                        sendMoveLargeAttack(m.getId(), m.getCfinal());
                        break;
                    case "EndLargeAttack":
                        sendEndLargeAttack(m.getId());
                        break;
                    case "CreateNewShortAttack":
                        //System.err.println("Sending new short attack");
                        sendCreateNewShortAttack(m.getId(), m.getId2(), m.getCfinal());
                        break;
                    case "EndShortAttack":
                        sendEndShortAttack(m.getId());
                        break;
                    case "NewCharacterLife":
                        sendNewCharacterLife(m.getId(), m.getLife());
                        break;
                    case "CharacterDeath":
                        sendCharacterDeath(m.getId(), m.getCfinal());
                        break;
                    case "GameEnded":
                        Thread.sleep(100); //cancer 3
                        endGame();
                        break;
                }
                System.out.println("adding " + m.getType());
                movs.add(m);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Initializes the game.
     *
     * @throws Exception
     */
    public void initializeGame() throws Exception {
        DataPacket packetToSend = new DataPacket();
        game.createMap();
        int idMap = dbc.addMap(game.getMap());
        int idGame = dbc.addGame(idMap);
        game.setIdGame(idGame);
        packetToSend.setOperation(Comms.SendMap);
        packetToSend.setData(game.getMap());
        server.broadcast(packetToSend);
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            game.selectCharacter(i);
            packetToSend.setOperation(Comms.SendCharacter);
            packetToSend.setData(game.getCharacter(i));
            packetToSend.setId(i);
            server.broadcast(packetToSend);
            dbc.addPlayer(game.getUsersName(i), i, game.getCharactersString(i), idGame);
            Thread.sleep(100); //cancer 1
        }
        Thread.sleep(100); //cancer 2
        packetToSend.setOperation(Comms.LinkStart);
        packetToSend.setData(null);
        game.setStartTime(System.currentTimeMillis());
        server.broadcast(packetToSend);
    }

    //Different send options.
    /**
     * Creates a packet and sends a change of direction.
     *
     * @param id
     * @param direction
     * @throws IOException
     */
    public void sendChangeDirection(int id, int direction) throws IOException {

        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.ChangeDirection);
        packetToSend.setId(id);
        packetToSend.setDirection(direction);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the move of a character.
     *
     * @param id
     * @param coord
     * @throws IOException
     */
    public void sendMoveCharacter(int id, Coordinate coord) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.MoveCharacter);
        packetToSend.setId(id);
        Coordinate c = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
        packetToSend.setData(c);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the creation of a new LargeAttack.
     *
     * @param id
     * @param id2
     * @param coord
     * @throws IOException
     */
    public void sendCreateNewLargeAttack(int id, int id2, Coordinate coord) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.CreateNewLargeAttack);
        packetToSend.setId(id);
        packetToSend.setId2(id2);
        Coordinate c = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
        packetToSend.setData(c);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the creation of a new LargeAttack.
     *
     * @param sc
     * @param id2
     * @throws IOException
     */
    public void sendCreateNewLargeAttack(SocketChannel sc, int id2) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.CreateNewLargeAttack);
        packetToSend.setId(game.getLargeAttack(id2).getCharacter().getId());
        packetToSend.setId2(id2);
        Coordinate coord = game.getLargeAttack(id2).getCoordinate();
        Coordinate c = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
        packetToSend.setData(c);
        if (game.isLargeAttackOn(id2)) {
            server.writeObject(packetToSend, sc);
        }
    }

    /**
     * Creates a packet and sends the movement of a LargeAttack.
     *
     * @param id
     * @param coord
     * @throws IOException
     */
    public void sendMoveLargeAttack(int id, Coordinate coord) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.MoveLargeAttack);
        packetToSend.setId(id);
        Coordinate c = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
        packetToSend.setData(c);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the end of a largeAttack.
     *
     * @param id
     * @throws IOException
     */
    public void sendEndLargeAttack(int id) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.EndLargeAttack);
        packetToSend.setId(id);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the end of a LargeAttack.
     *
     * @param sc
     * @param id
     * @throws IOException
     */
    public void sendEndLargeAttack(SocketChannel sc, int id) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.EndLargeAttack);
        packetToSend.setId(id);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends the creation of a new ShortAttack.
     *
     * @param id
     * @param id2
     * @param coord
     * @throws IOException
     */
    public void sendCreateNewShortAttack(int id, int id2, Coordinate coord) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.CreateNewShortAttack);
        packetToSend.setId(id);
        packetToSend.setId2(id2);
        Coordinate c = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
        packetToSend.setData(c);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the end of a ShortAttack.
     *
     * @param id
     * @throws IOException
     */
    public void sendEndShortAttack(int id) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.EndShortAttack);
        packetToSend.setId(id);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the character's life.
     *
     * @param id
     * @param life
     * @throws IOException
     */
    public void sendNewCharacterLife(int id, int life) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.NewCharacterLife);
        packetToSend.setId(id);
        packetToSend.setId2(life);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the death of a character.
     *
     * @param id
     * @param coord
     * @throws Exception
     */
    public void sendCharacterDeath(final int id, Coordinate coord) throws Exception {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.CharacterDeath);
        packetToSend.setId(id);
        Coordinate c = new Coordinate(coord.getX(), coord.getY(), coord.getObstacle(), null);
        packetToSend.setData(c);
        dbc.addScore(System.currentTimeMillis() - game.getStartTime(),
                position, game.getCharacter(id).getInflingedShoots(),
                game.getCharacter(id).getReceivedShoots(), game.getCharacter(id).getKills(),
                game.getUsersName(id), game.getIdGame());
        position++;
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the registration of a new user.
     *
     * @param sc
     * @throws IOException
     */
    public void sendRegisterNewUser(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.RegisterNewUser);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends the login of a user.
     *
     * @param sc
     * @throws IOException
     */
    public void sendLoginUser(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.LinkStartUser);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends the start of the game.
     *
     * @param sc
     * @param numPlayer
     * @param user
     * @throws Exception
     */
    public void sendStartGame(SocketChannel sc, int numPlayer, String user) throws Exception {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.StartGame);
        game.setUsersName(numPlayer, user);
        ArrayList usableChar = dbc.getUsableCharacters(user);
        packetToSend.setId(numPlayer);
        packetToSend.setData(usableChar);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends the denial of a game.
     *
     * @param sc
     * @throws IOException
     */
    public void sendGameDenied(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.NoGamesAvaliable);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends the records.
     *
     * @param sc
     * @param name
     * @throws Exception
     */
    public void sendRecords(SocketChannel sc, String name) throws Exception {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.WatchRecords);
        //gets the 10 best scores of the game and the best 5 of the user
        String general[] = dbc.getScoreBoard();
        String user[] = dbc.getUserScores(name);
        String scores[] = new String[Comms.numScoresGeneral + Comms.numScoresUser];
        System.arraycopy(general, 0, scores, 0, Comms.numScoresGeneral);
        for (int i = Comms.numScoresGeneral; i < Comms.numScoresGeneral + Comms.numScoresUser; i++) {
            scores[i] = user[i - Comms.numScoresGeneral];
        }
        packetToSend.setData(scores);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends old games.
     *
     * @param sc
     * @param user
     * @throws Exception
     */
    public void sendOldGames(SocketChannel sc, String user) throws Exception {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.WatchOldGames);
        int gameId = dbc.getIdGamePlayed(user);
        ArrayList record = new ArrayList<>();
        String[] scores = dbc.getGameScores(gameId);
        ArrayList mov = dbc.getRecord(gameId);
        if (!mov.isEmpty()) {
            OurMap m = dbc.getMap(gameId);
            ArrayList characters = dbc.getCharacterNames(gameId);
            packetToSend.setId(characters.size());
            record.add(0, m);
            record.addAll(Arrays.asList(scores));
            for (Object o : characters) {
                record.add(o);
            }
            for (Object o : mov) {
                record.add(o);
            }
        }
        packetToSend.setData(record);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends no name.
     *
     * @param sc
     * @throws IOException
     */
    public void sendNoName(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.NoName);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends existing user.
     *
     * @param sc
     * @throws IOException
     */
    public void sendExistingUser(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.ExistingUser);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends wrong password.
     *
     * @param sc
     * @throws IOException
     */
    public void sendWrongPassword(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.WrongPassword);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends the change of the password.
     *
     * @param sc
     * @throws IOException
     */
    public void sendPasswordChanged(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.PasswordChanged);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Creates a packet and sends error.
     *
     * @param sc
     * @throws IOException
     */
    public void sendError(SocketChannel sc) throws IOException {
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.Error);
        server.writeObject(packetToSend, sc);
    }

    /**
     * Saves the information of the game to the database and then creates a
     * packet and sends the end of the game.
     *
     * @throws Exception
     */
    public void endGame() throws Exception {
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            if (game.getCharacter(i).isAlive()) {
                dbc.addScore(System.currentTimeMillis() - game.getStartTime(),
                        position, game.getCharacter(i).getInflingedShoots(),
                        game.getCharacter(i).getReceivedShoots(), game.getCharacter(i).getKills(),
                        game.getUsersName(i), game.getIdGame());
            }
        }
        position = 0;
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.GameEnded);
        server.broadcast(packetToSend);
    }

    /**
     * Creates a packet and sends the scores of the match.
     *
     * @param sc
     * @throws Exception
     */
    public void sendMatchScores(SocketChannel sc) throws Exception {
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            DataPacket packetToSend = new DataPacket();
            packetToSend.setId(i);
            packetToSend.setId2(game.getIdGame());
            packetToSend.setData(dbc.getGameScores(game.getIdGame()));
            if (game.getCharacter(i).isAlive()) {
                packetToSend.setOperation(Comms.GameWinned);
                dbc.setUserRecord(game.getIdGame(), game.getUsersName(i));
            } else {
                packetToSend.setOperation(Comms.GameLost);
            }
            server.writeObject(packetToSend, sc);
            Thread.sleep(100);
        }
        server.removeClient(sc);
    }

    /**
     * Creates a packet and sends the removal of the records.
     *
     * @param sc
     * @param idGame
     * @throws Exception
     */
    public void sendRemoveRecords(SocketChannel sc, int idGame) throws Exception {
        dbc.removeCoordinates(idGame);
        movs.clear();
        DataPacket packetToSend = new DataPacket();
        packetToSend.setOperation(Comms.RemoveRecord);
        server.writeObject(packetToSend, sc);
        if (server.getClients().isEmpty()) {
            game.initialize();
            controllerReceive.initialize();
        }
    }

    /**
     * Deletes the previous saved game info, saves the new one then creates a
     * packet and sends the saving of the records.
     *
     * @param sc
     * @param user
     * @param idGame
     * @throws Exception
     */
    public void sendSaveRecords(SocketChannel sc, String user, int idGame) throws Exception {
        //deletes the previous saved game info
        int id = dbc.getIdGamePlayed(user);
        dbc.removeRecord(user);
        dbc.removeCoordinates(id);
        //saves the new one
        int i = dbc.addMovements(idGame, movs);
        movs.clear();
        dbc.setUserRecord(idGame, user);
        DataPacket packetToSend = new DataPacket();
        if (i != -1) {
            packetToSend.setOperation(Comms.SavedRecord);
        } else {
            packetToSend.setOperation(Comms.Error);
        }
        server.writeObject(packetToSend, sc);
        if (server.getClients().isEmpty()) {
            game.initialize();
            controllerReceive.initialize();
        }
    }
}
