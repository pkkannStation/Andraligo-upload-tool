/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aut.view.uploadmovie;

import aut.utils.FTPUtil;
import aut.view.View;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.commons.net.io.CopyStreamAdapter;
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
    private TextField titleTextField;
    @FXML
    private ComboBox<String> qualityComboBox;
    @FXML
    private Label urlLabel;
    @FXML
    private Label qualityLabel;

    private File file;
    
    private DoubleProperty progress;

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
        urlLabel.setTextFill(Color.BLACK);
        titleTextField.setStyle("");
        qualityLabel.setText("");

        boolean validated = true;

        if (file == null) {
            urlLabel.setTextFill(Color.RED);
            urlLabel.setText("You need to choose a file to upload...");
            validated = false;
        }
        if (titleTextField.getText().isEmpty()) {
            titleTextField.setStyle("-fx-background: red");
            validated = false;
        }
        if (qualityComboBox.getSelectionModel().getSelectedIndex() < 0) {
            qualityLabel.setTextFill(Color.RED);
            qualityLabel.setText("You need to choose a quality...");
            validated = false;
        }

        return validated;
    }

    @FXML
    private void handleUpload() {
        if (validate()) {
            progress = new SimpleDoubleProperty(0);
            int progress = 0;
            Runnable task = new Runnable() {

                @Override
                public void run() {
                    CopyStreamAdapter streamListener = new CopyStreamAdapter() {

                        @Override
                        public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                            //this method will be called everytime some bytes are transferred

                            double percent = (double) (totalBytesTransferred * 100 / file.length());
                            UploadMovieViewController.this.progress.set(percent);
                        }

                    };

                    FTPUtil.uploadFile(file, titleTextField.getText(), streamListener, getFileExtension(file));
                }
            };
            Runnable success = new Runnable() {

                @Override
                public void run() {
                    Dialogs.create().title("Success").message("Movie got uploaded successfully!").showInformation();
                    UploadMovieViewController.this.viewController.showMainView();
                }
            };
            Runnable fail = new Runnable() {

                @Override
                public void run() {
                    Dialogs.create().title("Fail").message("Could not upload movie :(").showError();
                    UploadMovieViewController.this.viewController.showMainView();
                }
            };
            Runnable cancel = new Runnable() {

                @Override
                public void run() {
                    UploadMovieViewController.this.viewController.showMainView();
                }
            };
            viewController.showProgressView(task, this.progress, success, fail, cancel);
        }
    }

    @FXML
    private void handleclear() {
        Action response = Dialogs.create().title("Clear?").message("Are you sure you want to clear?").showConfirm();
        if (response == Actions.YES) {
            urlLabel.setText("");
            qualityComboBox.getSelectionModel().clearSelection();
            titleTextField.setText("");
            urlLabel.setTextFill(Color.BLACK);
            titleTextField.setStyle("");
            qualityLabel.setText("");
            this.file = null;
        }

    }

}
