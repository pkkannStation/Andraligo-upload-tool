/**
 * Copyright (c) 2013, 2014 ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.controlsfx.dialog;

import static org.controlsfx.tools.Platform.OSX;
import static org.controlsfx.tools.Platform.UNIX;

import java.util.EnumSet;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.controlsfx.tools.Platform;

// Not public API (class is package-protected), so no JavaDoc is required.
@Deprecated
class HeavyweightDialog extends FXDialog {

    /**************************************************************************
     * 
     * Private fields
     * 
     **************************************************************************/
    
    private final Stage stage;
    private final Window owner;
    
    private static EnumSet<Platform> DECORATED_STAGE_PLATFORMS = EnumSet.of(OSX, UNIX);

    
    
    /**************************************************************************
     * 
     * Constructors
     * 
     **************************************************************************/
    
    HeavyweightDialog(String title, Window owner) {
        super();
        this.owner = owner;
        
        stage = new Stage() {
            @Override public void showAndWait() {
                centerOnScreen();
                super.showAndWait();
            }
            
            @Override public void centerOnScreen() {
                Window owner = getOwner();
                if (owner != null) {
                    Scene scene = owner.getScene();

                    if (scene != null) {
                        // scene.getY() seems to represent the y-offset from the top of the titlebar to the
                        // start point of the scene, so it is the titlebar height
                        final double titleBarHeight = scene.getY();
                        
                        // because Stage does not seem to centre itself over its owner, we
                        // do it here.
                        double x, y;
                        
                        final double dialogWidth = root.prefWidth(-1);
                        final double dialogHeight = root.prefHeight(-1);
                        
                        if (owner.getX() < 0 || owner.getY() < 0) {
                            // Fix for #165
                            Screen screen = Screen.getPrimary(); // todo something more sensible
                            double maxW = screen.getVisualBounds().getWidth();
                            double maxH = screen.getVisualBounds().getHeight();
                            
                            x = maxW / 2.0 - dialogWidth / 2.0;
                            y = maxH / 2.0 - dialogHeight / 2.0 + titleBarHeight;
                        } else {
                            x = owner.getX() + (scene.getWidth() / 2.0) - (dialogWidth / 2.0);
                            y = owner.getY() +  titleBarHeight + (scene.getHeight() / 2.0) - (dialogHeight / 2.0);
                        }
                        
                        setX(x);
                        setY(y);
                    }
                }
            }
        };

        if (owner != null) {
            stage.initOwner(owner);
        }
        
        init(title);

        boolean isCrossPlatformStyleSet = isCrossPlatformStyleClassSet();

        // *** The rest is for adding window decorations ***
        setCrossPlatformStyleEnabled(isCrossPlatformStyleSet);
        
        lightweightDialog.getStyleClass().add("heavyweight"); //$NON-NLS-1$

        Scene scene = new Scene(lightweightDialog);
        scene.getStylesheets().addAll(DIALOGS_CSS_URL.toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
    }
    
    @Override protected void setCrossPlatformStyleEnabled(boolean enabled) {
        super.setCrossPlatformStyleEnabled(enabled);
        
        if (enabled) {
            stage.initStyle(StageStyle.TRANSPARENT);
        
            // add window dragging
            dialogTitleBar.setOnMousePressed(event -> {
                mouseDragDeltaX = event.getSceneX();
                mouseDragDeltaY = event.getSceneY();
            });
            dialogTitleBar.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - mouseDragDeltaX);
                stage.setY(event.getScreenY() - mouseDragDeltaY);
            });
    
            // support maximising the dialog
            maxButton.setOnAction(new EventHandler<ActionEvent>() {
                private double restoreX;
                private double restoreY;
                private double restoreW;
                private double restoreH;
    
                @Override public void handle(ActionEvent event) {
                    Screen screen = Screen.getPrimary(); // todo something more sensible
                    double minX = screen.getVisualBounds().getMinX();
                    double minY = screen.getVisualBounds().getMinY();
                    double maxW = screen.getVisualBounds().getWidth();
                    double maxH = screen.getVisualBounds().getHeight();
    
                    if (restoreW == 0 || stage.getX() != minX || stage.getY() != minY || stage.getWidth() != maxW || stage.getHeight() != maxH) {
                        restoreX = stage.getX();
                        restoreY = stage.getY();
                        restoreW = stage.getWidth();
                        restoreH = stage.getHeight();
                        stage.setX(minX);
                        stage.setY(minY);
                        stage.setWidth(maxW);
                        stage.setHeight(maxH);
                    } else {
                        stage.setX(restoreX);
                        stage.setY(restoreY);
                        stage.setWidth(restoreW);
                        stage.setHeight(restoreH);
                    }
                }
            });
    
            // add window resizing
            EventHandler<MouseEvent> resizeHandler = new EventHandler<MouseEvent>() {
                private double width;
                private double height;
                private Point2D dragAnchor;
    
                @Override public void handle(MouseEvent event) {
                    EventType<? extends MouseEvent> type = event.getEventType();
    
                    if (type == MouseEvent.MOUSE_PRESSED) {
                        width = stage.getWidth();
                        height = stage.getHeight();
                        dragAnchor = new Point2D(event.getSceneX(), event.getSceneY());
                    } else if (type == MouseEvent.MOUSE_DRAGGED) {
                        stage.setWidth(Math.max(lightweightDialog.minWidth(-1),   width  + (event.getSceneX() - dragAnchor.getX())));
                        stage.setHeight(Math.max(lightweightDialog.minHeight(-1), height + (event.getSceneY() - dragAnchor.getY())));
                    }
                }
            };
            resizeCorner.setOnMousePressed(resizeHandler);
            resizeCorner.setOnMouseDragged(resizeHandler);
        }
    }
    
    @Override protected void setNativeStyleEnabled(boolean enabled) {
        super.setNativeStyleEnabled(enabled);
        if (enabled) {
            StageStyle style = DECORATED_STAGE_PLATFORMS.contains(Platform.getCurrent()) ? 
                  StageStyle.DECORATED : StageStyle.UTILITY;
            stage.initStyle(style);
        }
    }
    
    @Override protected void setUndecoratedStyleEnabled(boolean enabled) {
        super.setNativeStyleEnabled(enabled);
        if (enabled) {
            stage.initStyle(StageStyle.UNDECORATED);
        }
    }
    



    /**************************************************************************
     * 
     * Public API
     * 
     **************************************************************************/
    
    @Override public void setModal(boolean modal) {
        this.modal = modal;
        
        if (modal) {
            if (owner != null) {
                stage.initModality(Modality.WINDOW_MODAL);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }
        } else {
            stage.initModality(Modality.NONE);
        }
    }
    
    @Override 
    public ObservableList<String> getStylesheets(){
        return stage.getScene().getStylesheets();
    }
    
    @Override public boolean isModal() {
        return modal;
    }
    
    @Override public void setContentPane(Pane pane) {
        root.setCenter(pane);
    }

    @Override public void setIconifiable(boolean iconifiable) {
        if (minButton != null) {
            minButton.setVisible(iconifiable);
        }
    }
    
    @Override public void setClosable(boolean closable) {
        if (closeButton != null) {
            closeButton.setVisible(closable);
        }
    }
    
    @Override public void show() {
        root.heightProperty().addListener(o -> stage.centerOnScreen());
        stage.showAndWait();
    }
    
    @Override public void hide() {
        stage.hide();
    }
    
    @Override public Window getWindow() {
        return stage;
    }
    
    @Override public Node getRoot() {
        return stage.getScene().getRoot();
    }
    
    @Override public double getX() {
        return stage.getX();
    }
    
    @Override public void setX(double x) {
        stage.setX(x);
    }
    
    @Override ReadOnlyDoubleProperty heightProperty() {
        return stage.heightProperty();
    }
    
    @Override ReadOnlyDoubleProperty widthProperty() {
        return stage.widthProperty();
    }
    
    @Override BooleanProperty resizableProperty() {
        return stage.resizableProperty();
    }
    
    @Override StringProperty titleProperty() {
        return stage.titleProperty();
    }
    
    @Override ReadOnlyBooleanProperty focusedProperty() {
        return stage.focusedProperty();
    }
    
    @Override public void sizeToScene() {
        stage.sizeToScene();
    }
    
    @Override void setIconified(boolean iconified) {
        stage.setIconified(iconified);
    }
    
    @Override boolean isIconified() {
        return stage.isIconified();
    }
    
    @Override
    void setEffect(Effect e) {
        // not implemented for heavyweight dialogs
    }
}
