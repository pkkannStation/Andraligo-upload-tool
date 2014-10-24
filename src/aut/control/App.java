
package aut.control;

import aut.db.MovieDAO;
import aut.model.MovieRegister;
import aut.utils.DBUtil;
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
    
    private IDHandler idHandler;
    
    private MovieDAO movieDAO;
    private MovieRegister movieRegister;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initModel();
        this.viewController = new ViewController(primaryStage, movieRegister);
        this.viewController.initFrame();
        this.viewController.showMainView();
    }
    
    public void initModel() {
        idHandler = new IDHandler();
        
        movieDAO = new MovieDAO();
        movieRegister = new MovieRegister(movieDAO, idHandler);
        movieRegister.load();
        
        DBUtil.close();
        
        idHandler.init(movieRegister);
        idHandler.refresh();
    }
    
    public static void main(String[] args ) {
        launch(args);
    }

}
