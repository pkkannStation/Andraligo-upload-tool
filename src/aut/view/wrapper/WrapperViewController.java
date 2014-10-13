package aut.view.wrapper;

import aut.view.View;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Patrick
 */
public class WrapperViewController extends View implements Initializable {
    
    @FXML
    private BorderPane mainPane;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPane(mainPane);
        setViewController(viewController);
    }
    
}
