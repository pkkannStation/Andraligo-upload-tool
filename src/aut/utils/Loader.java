package aut.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Patrick
 */
public class Loader {

    private Loader() {
    }

    public static void showSimpleLoader(Object owner, String title, String message, Runnable task, Runnable success, Runnable fail) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws InterruptedException {
                        updateMessage(message);
                        if (task != null) {
                            task.run();
                        }
                        return null;
                    }
                };
            }
        };

        Dialogs.create()
                .owner(owner)
                .title(title)
                .showWorkerProgress(service);

        service.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                if (fail != null) {
                    fail.run();
                }
            }
        });
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                if (success != null) {
                    success.run();
                }
            }
        });

        service.start();
    }

    public static void showProgressLoader(Object owner, String title, String masterhead, DoubleProperty progress, Runnable task, Runnable success, Runnable fail) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws InterruptedException {
                        updateMessage(progress.get() + "%");
                        if (task != null) {
                            progress.addListener(new ChangeListener<Number>() {

                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                    updateProgress(progress.get(), 100);
                                    updateMessage(progress.get() + "%");
                                }
                            });
                            task.run();
                        }
                        return null;
                    }
                };
            }
        };

        Dialogs.create()
                .owner(owner)
                .title(title)
                .masthead(masterhead)
                .showWorkerProgress(service);

        service.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                if (fail != null) {
                    fail.run();
                }
            }
        });
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                if (success != null) {
                    success.run();
                }
            }
        });

        service.start();
    }

}
