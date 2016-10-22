package utilities.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import utilities.map.OurMap;
import utilities.communications.Comms;
import utilities.image.TreatImage;

public class GMap implements Graphics {

    /**
     * This class only has the images of the map obstacles.
     */
    private BufferedImage obs[];
    private OurMap map;

    private final GraphicsGame graphics;

    /**
     * The constructor of this class uploads the images of the map and shows
     * them.
     *
     * @param m is the map with it's coordinates.
     * @param g
     */
    public GMap(OurMap m, GraphicsGame g) {
        map = m;
        graphics = g;
        obs = new BufferedImage[Comms.numObstacles + 1];
        try {
            BufferedImage background = ImageIO.read(new File("maps/background" + m.getType() + ".png"));
            obs[0] = background;
            for (int j = 1; j < Comms.numObstacles; ++j) {
                obs[j] = ImageIO.read(new File("maps/obstacle" + m.getType() + j + ".png"));
                obs[j] = TreatImage.combine(obs[j], background);
            }
            obs[Comms.numObstacles] = ImageIO.read(new File("maps/newGrave.png"));
            obs[Comms.numObstacles] = TreatImage.combine(obs[Comms.numObstacles], background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeSprite() {

    }

    //Getters
    /**
     * Gets obs.
     *
     * @return obs.
     */
    public BufferedImage[] getObs() {
        return obs;
    }

    /**
     * Gets map.
     *
     * @return map.
     */
    public OurMap getMap() {
        return map;
    }

    //Setters
    /**
     * Sets obs.
     *
     * @param obs
     */
    public void setObs(BufferedImage[] obs) {
        this.obs = obs;
    }

    /**
     * Sets map.
     *
     * @param map
     */
    public void setMap(OurMap map) {
        this.map = map;
    }

}
