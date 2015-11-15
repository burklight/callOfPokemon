package server;

import characters.*;
import java.awt.*;

import javax.swing.*;
import keyboard.KeyInput;
import map.OurMap;

public class Game {

    private static JFrame window;
    private static Container container;
    private static OurCharacter character;
    private static OurMap map;
    
    public static void main(String args[]) throws Exception{
        graphics();
        map.show();
        character.show();
        window.addKeyListener(new KeyInput(character));
    }
    
    private static void graphics(){
        try {
            window = new JFrame();
            
            window.setMinimumSize(new Dimension(Comms.windowWidth,Comms.windowHeight));
            window.setMaximumSize(new Dimension(Comms.windowWidth,Comms.windowHeight));
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            container = window.getContentPane();
            /*GridLayout gl = new GridLayout(Comms.obstacleColumns,Comms.obstacleRows);
            container.setLayout(gl);*/
            container.setLayout(new BorderLayout());
            map = new OurMap(container);
            character = new Pikachu(container,map);

            window.setLocationRelativeTo(null);
            window.setVisible(true);
        } catch (Exception ex) {}

        
    }
}
