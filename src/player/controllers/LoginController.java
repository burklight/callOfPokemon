package player.controllers;

import player.models.LoginModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import utilities.communications.Comms;
import utilities.communications.Communications;
import utilities.communications.DataPacket;

/**
 * This class controls the login and register field.
 */
public class LoginController {

    private JFrame window;
    private JButton register, login;
    private JTextField nameField;
    private JPasswordField passwordField;
    private final LoginModel model;
    private Communications coms;

    public LoginController(LoginModel m) {
        model = m;
        nameField = new JTextField();
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        register = new JButton();
        login = new JButton();
    }

    /**
     * Sets RegisterListener.
     */
    public void setRegisterListener() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                model.setName(nameField.getText());
                model.setPassword(passwordField.getText());
                nameField.setText("");
                passwordField.setText("");
                DataPacket packet = new DataPacket();
                packet.setOperation(Comms.RegisterNewUser);
                String data[] = {model.getName(), model.getPassword()};
                packet.setData(data);
                try {
                    coms.send(packet);
                    packet = coms.receive();
                    model.setOperation(packet.getOperation());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                model.infoEntered();
            }
        };
        register.addActionListener(a);
    }

    /**
     * Sets LoginListener.
     */
    public void setLoginListener() {
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                sendPersonalInfo();
            }
        };
        login.addActionListener(a);
    }

    /**
     * Sets PasswordListener.
     */
    public void setPasswordListener() {
        AbstractAction act = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendPersonalInfo();
            }
        };
        passwordField.addActionListener(act);
    }

    /**
     * Sets NameListener.
     */
    public void setNameListener() {
        AbstractAction act = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendPersonalInfo();
            }
        };
        nameField.addActionListener(act);
    }

    /**
     * Sets WindowListener.
     */
    public void setWindowListener() {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                DataPacket packet = new DataPacket();
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
     * Sends PersonalInfo (Name and password).
     */
    private void sendPersonalInfo() {
        model.setName(nameField.getText());
        model.setPassword(passwordField.getText());
        passwordField.setText("");
        DataPacket packet = new DataPacket();
        packet.setOperation(Comms.LinkStartUser);
        String data[] = {model.getName(), model.getPassword()};
        packet.setData(data);
        try {
            coms.send(packet);
            packet = coms.receive();
            model.setOperation(packet.getOperation());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.infoEntered();

    }

    //Getters
    /**
     * Gets register.
     *
     * @return register.
     */
    public JButton getRegister() {
        return register;
    }

    /**
     * Gets login.
     *
     * @return login.
     */
    public JButton getLogin() {
        return login;
    }

    /**
     * Gets nameField.
     *
     * @return nameField.
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * Gets passwordField.
     *
     * @return passwordField.
     */
    public JPasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * Gets window.
     *
     * @return window.
     */
    public JFrame getWindow() {
        return window;
    }

    //Setters
    /**
     * Sets register.
     *
     * @param register
     */
    public void setRegister(JButton register) {
        this.register = register;
    }

    /**
     * Sets login.
     *
     * @param login
     */
    public void setLogin(JButton login) {
        this.login = login;
    }

    /**
     * Sets nameField.
     *
     * @param nameField
     */
    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    /**
     * Sets passwordField.
     *
     * @param passwordField
     */
    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    /**
     * Sets coms.
     *
     * @param coms
     */
    public void setComs(Communications coms) {
        this.coms = coms;
    }

    /**
     * Sets window.
     *
     * @param window
     */
    public void setWindow(JFrame window) {
        this.window = window;
    }

}
