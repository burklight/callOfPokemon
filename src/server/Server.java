package server;

import server.controllers.ReceiveController;
import server.controllers.SendController;
import server.models.GameModel;
import utilities.communications.ServerNIO;

/**
 * This class starts a new server.
 */
public class Server {

    public static void main(String[] args) throws Exception {

        ServerNIO server = new ServerNIO();

        GameModel game = new GameModel();

        SendController controllerSend = new SendController(server, game);
        ReceiveController controllerReceive = new ReceiveController(server, game, controllerSend);
        controllerSend.setControllerReceive(controllerReceive);

        controllerReceive.start();
        controllerSend.start();
    }
}
