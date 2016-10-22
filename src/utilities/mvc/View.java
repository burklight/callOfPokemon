package utilities.mvc;

import java.util.Observer;
import javax.swing.JComponent;

public interface View extends Observer {

    public JComponent getView();

}
