package utilities.sounds;

public class SoundsExecutor {

    private static Thread t;

    public SoundsExecutor() {

    }

    public void execute(Runnable r) {
        t = new Thread(r);
        t.start();
    }

    //Couldn't find any better solution
    public void end() {
        t.stop();
    }

}
