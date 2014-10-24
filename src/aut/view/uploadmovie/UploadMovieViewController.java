/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aut.view.uploadmovie;

import aut.model.Movie;
import aut.utils.FTPUtil;
import aut.view.View;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog.Actions;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Patrick
 */
public class UploadMovieViewController extends View implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button browseButton;
    @FXML
    private Button uploadButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button newButton;
    @FXML
    private ListView<Movie> searchListView;
    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<String> qualityComboBox;
    @FXML
    private Label urlLabel;

    private File file;
    
    private DoubleProperty progress;
    
    private FTPUtil ftp;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPane(mainPane);
        setViewController(viewController);

        //init quality combobox
        qualityComboBox.getItems().addAll(
                "DVD",
                "720p",
                "1080p",
                "720p 3D",
                "1080p 3D"
        );
        
        
    }
    
    public void initContent() {
        //Set listview
        searchListView.setItems(viewController.movieRegister.getObservableObjects());
    }
    
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    @FXML
    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose movie file");
        this.file = fileChooser.showOpenDialog(viewController.primaryStage);

        if (file != null) {
            urlLabel.setText(this.file.getAbsolutePath());
            urlLabel.setTextFill(Color.BLACK);
        }
    }

    private boolean validate() {
        boolean validated = true;

        if (file == null) {
            validated = false;
        }
        if (titleTextField.getText().isEmpty()) {
            validated = false;
        }
        if (qualityComboBox.getSelectionModel().getSelectedIndex() < 0) {
            validated = false;
        }

        return validated;
    }
    
    private void clear() {
        
    }

    @FXML
    private void handleUpload() {
        if (validate()) {
            
        }
    }

    @FXML
    private void handleclear() {
        Action response = Dialogs.create().title("Clear?").message("Are you sure you want to clear?").showConfirm();
        if (response == Actions.YES) {
            clear();
            this.file = null;
        }

    }
    
    @FXML
    private void handleNew() {
        
    }

}
