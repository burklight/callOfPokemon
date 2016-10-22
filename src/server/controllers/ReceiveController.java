package server.controllers;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import server.models.GameModel;
import utilities.communications.ServerNIO;
import utilities.communications.Comms;
import utilities.communications.DataPacket;

/**
 * This class controls the received messages in the server. It processes the
 * messages or not and sends them to the SendController.
 */
public class ReceiveController extends Thread {

    protected ServerNIO server;
    protected GameModel game;
    protected int charactersSelected, playersConnected;
    protected boolean allCharactersSelected, maxPlayersReached;
    protected boolean[] numPlayerAvaiable;
    //I know this is really random, but I think it makes sense
    //since there are control messages that need to be sent immediately
    protected SendController controllerSend;
    protected DataBaseController dbc;

    public ReceiveController(ServerNIO s, GameModel g, SendController c) {
        server = s;
        game = g;
        dbc = server.getDbc();
        controllerSend = c;
        charactersSelected = 0;
        playersConnected = 0;
        maxPlayersReached = false;
        allCharactersSelected = false;
        numPlayerAvaiable = new boolean[Comms.NUM_PLAYERS];
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            numPlayerAvaiable[i] = true;
        }
    }

    /**
     * Initializes the variables.
     */
    public void initialize() {
        charactersSelected = 0;
        playersConnected = 0;
        maxPlayersReached = false;
        allCharactersSelected = false;
        numPlayerAvaiable = new boolean[Comms.NUM_PLAYERS];
        for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
            numPlayerAvaiable[i] = true;
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                System.out.println("Waiting for select...");
                int noOfKeys = server.getSelector().select();

                System.out.println("Number of selected keys: " + noOfKeys);

                Set<SelectionKey> selKeys = server.getSelector().selectedKeys();
                Iterator<SelectionKey> it = selKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    if (key.isAcceptable()) {
                        doAccept(key);
                    } else if (key.isReadable()) {
                        processKey(key);
                    }
                    it.remove();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Accepts a new connection from a client.
     *
     * @param sk
     * @throws IOException
     */
    public void doAccept(SelectionKey sk) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) sk.channel()).accept();
        sc.configureBlocking(false);
        sc.register(server.getSelector(), SelectionKey.OP_READ);
        System.out.println("Accepted new connection from client: " + sc);
        DataPacket packet = new DataPacket();
        packet.setOperation(Comms.AcceptedConnection);
        server.writeObject(packet, sc);
    }

    /**
     * Processes the given selectionKey and performs the actions that must be
     * done in each case.
     *
     * @param sk
     * @throws Exception
     */
    public void processKey(SelectionKey sk) throws Exception {
        SocketChannel sc = (SocketChannel) sk.channel();
        DataPacket packet = (DataPacket) server.readByteBuffer(sc);
        System.err.println(packet.getOperation());
        switch (packet.getOperation()) {
            case Comms.EndConnection:
                sk.channel().close();
                sk.cancel();
                if (server.getClients().contains(sc)) {
                    server.removeClient(sc);
                    playersConnected--;
                    numPlayerAvaiable[packet.getId()] = true;
                    //if the game is being played
                    if (allCharactersSelected) {
                        game.kill(packet.getId());
                        if (playersConnected == 0) {
                            maxPlayersReached = false;
                            allCharactersSelected = false;
                        }
                    } //if the players are in the selection character screen
                    else if (maxPlayersReached) {
                        maxPlayersReached = false;
                    }
                }
                break;
            case Comms.RegisterNewUser: {
                String data[] = (String[]) packet.getData();
                //to do
                //register new user
                //database things
                if (data[0].equals("")) {
                    controllerSend.sendNoName(sc);
                } else if (dbc.registerUser(data[0], data[1]) < 0) {
                    controllerSend.sendExistingUser(sc);
                } else {
                    controllerSend.sendRegisterNewUser(sc);
                }
                break;
            }
            case Comms.LinkStartUser: {
                String data[] = (String[]) packet.getData();
                //to do
                //login user
                //database things
                if (data[0].equals("")) {
                    controllerSend.sendNoName(sc);
                } else if (dbc.loginUser(data[0], data[1]) < 0) {
                    controllerSend.sendWrongPassword(sc);
                } else {
                    controllerSend.sendLoginUser(sc);
                }
                break;
            }
            case Comms.StartGameRequest:
                //check number of players in character selection screen
                if (!maxPlayersReached) {
                    server.addClient(sc);
                    int numPlayer = -1;
                    for (int i = 0; i < Comms.NUM_PLAYERS; i++) {
                        if (numPlayerAvaiable[i]) {
                            numPlayer = i;
                            numPlayerAvaiable[i] = false;
                            break;
                        }
                    }
                    controllerSend.sendStartGame(sc, numPlayer, (String) packet.getData());
                    playersConnected++;
                    if (playersConnected == Comms.NUM_PLAYERS) {
                        maxPlayersReached = true;
                    }
                } else {
                    controllerSend.sendGameDenied(sc);
                }
                break;
            case Comms.WatchRecordsRequest: {
                String name = (String) packet.getData();
                controllerSend.sendRecords(sc, name);
                break;
            }
            case Comms.WatchOldGamesRequest: {
                String name = (String) packet.getData();
                controllerSend.sendOldGames(sc, name);
                break;
            }
            case Comms.ChangePassword: {
                String user[] = (String[]) packet.getData();
                if (dbc.changePassword(user[0], user[1])) {
                    controllerSend.sendPasswordChanged(sc);
                } else {
                    controllerSend.sendError(sc);
                }
                break;
            }
            case Comms.CharacterSelected:
                String s = (String) packet.getData();
                game.setCharacterString(packet.getId(), s);
                charactersSelected++;
                if (charactersSelected == Comms.NUM_PLAYERS) {
                    allCharactersSelected = true;
                    controllerSend.initializeGame();
                }
                break;
            case Comms.SendScoresRequest:
                controllerSend.sendMatchScores(sc);
                break;
            case Comms.RemoveRecord: {
                int idGame = packet.getId2();
                controllerSend.sendRemoveRecords(sc, idGame);
                break;
            }
            case Comms.SavedRecord: {
                String user = (String) packet.getData();
                int idGame = packet.getId2();
                controllerSend.sendSaveRecords(sc, user, idGame);
                break;
            }
            case Comms.ChangeDirection: {
                int id = packet.getId();
                if (!game.isEnded()) {
                    game.changeDirection(id, packet.getDirection());
                }
                break;
            }
            case Comms.MoveCharacter: {
                int id = packet.getId();
                if (!game.isEnded()) {
                    game.moveCharacter(id);
                }
                break;
            }
            case Comms.CreateNewLargeAttack: {
                int id = packet.getId();
                if (!game.isEnded()) {
                    game.createLargeAttack(id);
                }
                break;
            }
            case Comms.CreateNewShortAttack: {
                int id = packet.getId();
                if (!game.isEnded()) {
                    game.createShortAttack(id);
                }
                break;
            }
            case Comms.IsLargeAttackOn: {
                int id = packet.getId();
                if (!game.isLargeAttackOn(id)) {
                    controllerSend.sendEndLargeAttack(sc, id);
                }
                break;
            }
            case Comms.ReSendLargeAttack: {
                int id = packet.getId();
                controllerSend.sendCreateNewLargeAttack(sc, id);
                break;
            }
        }
    }

}
