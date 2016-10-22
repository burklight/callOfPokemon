package player;

import utilities.communications.ClientNIO;
import player.controllers.ViewsController;
import utilities.mvc.Model;
import utilities.communications.Communications;

/**
 * This class creates a new client that connects to the server to start the
 * game.
 */
public class Player {

    public static void main(String[] args) throws Exception {

        Communications clientCom = new ClientNIO();

        //Model (data)
        Model<Object> model = new Model<>();

        clientCom.waitConnection();

        //View (interface)
        final ViewsController<Object> vista = new ViewsController<>(model);
        vista.initializeControllerCommunications(clientCom);
    }
}
