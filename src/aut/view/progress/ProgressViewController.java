/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aut.view.progress;

import aut.view.View;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialog.Actions;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Patrick
 */
public class ProgressViewController extends View implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label rateLabel;
    @FXML
    private Label percentLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button cancelButton;

    private Runnable task;
    private Runnable success;
    private Runnable fail;
    private Runnable cancel;

    private DoubleProperty progress;

    private Service service;

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

        //progress.set(0.1);
        service = new Service() {

            @Override
            protected Task createTask() {
                return new Task() {

                    @Override
                    protected Object call() throws Exception {
                        progress.addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                updateProgress(progress.get(), 100);
                                updateMessage(progress.get() + "%");
                            }
                        });
                        task.run();
                        return null;
                    }
                };
            }
        };

        service.setOnSucceeded(new EventHandler() {

            @Override
            public void handle(Event event) {
                if (success != null) {
                    success.run();
                }
                System.out.println("Task succeded!");
            }
        });
        service.setOnFailed(new EventHandler() {

            @Override
            public void handle(Event event) {
                if (fail != null) {
                    fail.run();
                }
                System.err.println("Task failed...");
            }
        });
        service.setOnCancelled(new EventHandler() {

            @Override
            public void handle(Event event) {
                if (cancel != null) {
                    cancel.run();
                }
                System.out.println("Task canceled...");
            }
        });

        progressBar.progressProperty().bind(service.progressProperty());
        percentLabel.textProperty().bind(service.messageProperty());
    }

    public void start(Runnable task, DoubleProperty progress, Runnable success, Runnable fail, Runnable cancel) {
        this.progress = progress;
        this.task = task;
        this.success = success;
        this.fail = fail;
        this.cancel = cancel;
        service.start();
    }

    @FXML
    private void handleCancel() {
        if (this.service.isRunning()) {
            Action action = Dialogs.create().title("Sure?").message("Sure you want to cancel?").showConfirm();
            if (action == Actions.YES) {
                this.service.cancel();
            }
        }
    }

}
