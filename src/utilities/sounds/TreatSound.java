package utilities.sounds;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class TreatSound {

    private static Player player;
    private static final SoundsExecutor executor = new SoundsExecutor();

    public static void playSound(final String name) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        playSoundRunnable(name);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        executor.execute(r);
    }

    private static void playSoundRunnable(final String name) {
        try {
            String song = "sounds/" + name + ".mp3";
            FileInputStream input = new FileInputStream(song);
            BufferedInputStream bufferedInput = new BufferedInputStream(input);

            player = new Player(bufferedInput);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void endSound() {
        executor.end();
    }

}
