
package aut.control;

import aut.view.ViewController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Patrick
 */
public class App extends Application {
    
    public static final String name = "Andraligo Upload Tool";
    public static final String version = "v1.0";
    public static final String title = name + " - " + version;
    
    private ViewController viewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.viewController = new ViewController(primaryStage);
        this.viewController.initFrame();
        this.viewController.showMainView();
    }
    
    public static void main(String[] args ) {
        launch(args);
    }

}
