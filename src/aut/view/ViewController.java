package aut.view;

import aut.control.App;
import aut.model.MovieRegister;
import aut.view.main.MainViewController;
import aut.view.progress.ProgressViewController;
import aut.view.uploadmovie.UploadMovieViewController;
import aut.view.wrapper.WrapperViewController;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Patrick
 */
public class ViewController {

    public final Stage primaryStage;
    public BorderPane wrapper;
    
    public MovieRegister movieRegister;

    public ViewController(Stage primaryStage, MovieRegister movieRegister) {
        this.primaryStage = primaryStage;
        this.movieRegister = movieRegister;
    }

    public void initFrame() {
        if (this.primaryStage != null) {
            this.primaryStage.setMinWidth(630);
            this.primaryStage.setMinHeight(430);
            this.primaryStage.setMaxWidth(630);
            this.primaryStage.setMaxHeight(430);
            this.primaryStage.setTitle(App.title);
            this.primaryStage.setResizable(false);
            makeWrapperView();
            this.primaryStage.show();
        } else {
            throw new NullPointerException("PrimaryStage is null, can not initialise frame");
        }
    }

    public void makeWrapperView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewController.class.getResource("wrapper/WrapperView.fxml"));
            BorderPane pane = (BorderPane) loader.load();

            this.wrapper = pane;

            Scene scene = new Scene(pane);
            WrapperViewController controller = loader.getController();
            controller.setViewController(this);
            this.primaryStage.setScene(scene);
        } catch (IOException e) {

        }
    }
    
    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewController.class.getResource("main/MainView.fxml"));
            StackPane pane = (StackPane) loader.load();
            pane.setOpacity(0.0);

            MainViewController controller = loader.getController();
            controller.setViewController(this);
            controller.initTabs();
            this.wrapper.setCenter(pane);
            controller.fadeIn(300, null);
        } catch (IOException e) {

        }
    }

    public AnchorPane makeUploadMovieView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewController.class.getResource("uploadmovie/UploadMovieView.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();

            UploadMovieViewController controller = loader.getController();
            controller.setViewController(this);
            controller.initContent();

            return pane;
        } catch (IOException e) {

        }
        return null;
    }

    public AnchorPane makeUploadTVShowView() {

        return null;
    }

    public AnchorPane makeUploadComedyView() {

        return null;
    }

    public AnchorPane makeUploadMusicView() {

        return null;
    }

    public void showProgressView(Runnable task, IntegerProperty progress, Runnable success, Runnable fail, Runnable cancel) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewController.class.getResource("progress/ProgressView.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            pane.setOpacity(0.0);

            ProgressViewController controller = loader.getController();
            controller.setViewController(this);
            this.wrapper.setCenter(pane);
            controller.fadeIn(300, null);
            controller.start(task, progress, success, fail, cancel);
            
        } catch (IOException e) {

        }
    }

    public ViewController() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
