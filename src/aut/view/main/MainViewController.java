/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aut.view.main;

import aut.view.View;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Patrick
 */
public class MainViewController extends View implements Initializable {
    
    @FXML
    private StackPane mainPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab uploadMovieTab;
    @FXML
    private Tab uploadTVShowTab;
    @FXML
    private Tab uploadComedyTab;
    @FXML
    private Tab uploadMusicTab;

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
    
    public void initTabs() {
        uploadMovieTab.setContent(viewController.makeUploadMovieView());
//        uploadTVShowTab.setContent(viewController.makeUploadTVShowView());
//        uploadComedyTab.setContent(viewController.makeUploadTVShowView());
//        uploadMusicTab.setContent(viewController.makeUploadMusicView());
    }
    
}
