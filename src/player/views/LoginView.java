package player.views;

import player.controllers.LoginController;
import player.models.LoginModel;
import player.controllers.ViewsController;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.Observable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import utilities.mvc.View;
import utilities.communications.Comms;
import utilities.sounds.TreatSound;

/**
 * This class is the view used to register and login.
 */
public class LoginView implements View {

    private final ViewsController gui;

    private final JFrame window;
    private final Container container;
    private final JPanel panel0, panel1, panel2, panel3, panel4;
    private final JLabel Lgame, Lname, Lpassword;
    private final JButton register, login;
    private final JTextField playerName;
    private final JPasswordField password;

    private final LoginController controller;
    private final LoginModel model;

    public LoginView(ViewsController g) throws IOException {
        gui = g;

        model = new LoginModel();
        model.addObserver(this);
        controller = new LoginController(model);

        window = new JFrame();
        window.setVisible(false);
        window.setTitle("Insert your name and password.");

        window.setSize(new Dimension(350, 175));
        window.setResizable(false);

        container = window.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        panel0 = new JPanel();
        panel0.setLayout(new BorderLayout());
        panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel3 = new JPanel();
        panel3.setLayout(new FlowLayout());
        panel4 = new JPanel();

        Lgame = new JLabel();
        Lgame.setFont(new Font(Font.DIALOG, Font.BOLD, 21));
        Lgame.setText("Welcome to CALL OF POKEMON!");
        Lgame.setSize(new Dimension(350, 35));
        Lgame.setHorizontalAlignment(JLabel.CENTER);
        register = new JButton();
        register.setPreferredSize(new Dimension(155, 25));
        register.setText("Register");
        login = new JButton();
        login.setPreferredSize(new Dimension(155, 25));
        login.setText("Login");
        playerName = new JTextField();
        playerName.setPreferredSize(new Dimension(155, 25));
        playerName.setText("");
        Lname = new JLabel();
        Lname.setPreferredSize(new Dimension(155, 25));
        Lname.setText("Insert your name:");
        password = new JPasswordField();
        password.setPreferredSize(new Dimension(155, 25));
        password.setText("");
        Lpassword = new JLabel();
        Lpassword.setPreferredSize(new Dimension(155, 25));
        Lpassword.setText("Insert your password:");

        panel0.add(Lgame);
        panel1.add(Lname);
        panel1.add(playerName);
        panel2.add(Lpassword);
        panel2.add(password);
        panel3.add(register);
        panel3.add(login);

        controller.setWindow(window);
        controller.setRegister(register);
        controller.setLogin(login);
        controller.setNameField(playerName);
        controller.setPasswordField(password);

        controller.setWindowListener();
        controller.setRegisterListener();
        controller.setLoginListener();
        controller.setNameListener();
        controller.setPasswordListener();

        container.add(panel0);
        container.add(panel1);
        container.add(panel2);
        container.add(panel3);

        window.setLocationRelativeTo(null);
    }

    /**
     * Sets the window visible while playing the music theme "opening".
     */
    public void show() {
        TreatSound.playSound("opening");
        window.setVisible(true);
    }

    @Override
    public JComponent getView() {
        return null;
    }

    /**
     * Gets model.
     *
     * @return model.
     */
    public LoginModel getModel() {
        return model;
    }

    /**
     * Gets controler.
     *
     * @return controler.
     */
    public LoginController getController() {
        return controller;
    }

    /**
     * This method handles all the different options that could happend in the
     * login view. Shows a message informing of the case or contiues to the next
     * view if the login is done correctly.
     *
     * @param o
     * @param o1
     */
    @Override
    public void update(Observable o, Object o1) {
        switch (model.getOperation()) {
            case Comms.RegisterNewUser:
                gui.infoMessage(window, "User created correctly!", "Great!");
                break;
            case Comms.LinkStartUser:
                window.setVisible(false);
                gui.setUserName(model.getName());
                gui.showMenu(model.getName(), false);
                gui.setName(model.getName());
                break;
            case Comms.NoName:
                gui.errorMessage(window, "You forgot to write your name.");
                break;
            case Comms.WrongPassword:
                gui.errorMessage(window, "Check your username and password please.");
                break;
            case Comms.ExistingUser:
                gui.errorMessage(window, "This user name is already taken.");
        }
    }

    /**
     * Sets the title image
     *
     * @param i
     */
    public void setTitle(Image i) {
        window.setIconImage(i);
    }

}
