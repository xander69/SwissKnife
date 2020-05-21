package ru.xander.swissknife.util;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * @author Alexander Shakhov
 */
public abstract class Dialog {
    final Stage stage = new Stage() {
        @Override
        public void centerOnScreen() {
            Window owner = getOwner();
            if (owner != null) {
                positionStage();
            } else {
                if (getWidth() > 0 && getHeight() > 0) {
                    super.centerOnScreen();
                }
            }
        }
    };

    final AnchorPane dialogPane = new AnchorPane();
    private double prefX = Double.NaN;
    private double prefY = Double.NaN;

    private void positionStage() {
        double x = stage.getX();
        double y = stage.getY();

        // if the user has specified an x/y location, use it
        if (!Double.isNaN(x) && !Double.isNaN(y) &&
                Double.compare(x, prefX) != 0 && Double.compare(y, prefY) != 0) {
            // weird, but if I don't call setX/setY here, the stage
            // isn't where I expect it to be (in instances where a single
            // dialog is shown and closed multiple times). I expect the
            // second showing to be in the place the dialog was when it
            // was closed the first time, but on Windows it jumps to the
            // top-left of the screen.
            stage.setX(x);
            stage.setY(y);
            return;
        }

        // Firstly we need to force CSS and layout to happen, as the dialogPane
        // may not have been shown yet (so it has no dimensions)
        dialogPane.applyCss();
        dialogPane.layout();

        final Window owner = stage.getOwner();
        final Scene ownerScene = owner.getScene();

        // scene.getY() seems to represent the y-offset from the top of the titlebar to the
        // start point of the scene, so it is the titlebar height
        final double titleBarHeight = ownerScene.getY();

        // because Stage does not seem to centre itself over its owner, we
        // do it here.

        // then we can get the dimensions and position the dialog appropriately.
        final double dialogWidth = dialogPane.prefWidth(-1);
        final double dialogHeight = dialogPane.prefHeight(dialogWidth);

//        stage.sizeToScene();

        x = owner.getX() + (ownerScene.getWidth() / 2.0) - (dialogWidth / 2.0);
        y = owner.getY() + titleBarHeight / 2.0 + (ownerScene.getHeight() / 2.0) - (dialogHeight / 2.0);

        prefX = x;
        prefY = y;

        stage.setX(x);
        stage.setY(y);
    }

    public void showAndWait() {
        this.stage.showAndWait();
    }

    public static MessageDialog.Configurer message() {
        return new MessageDialog.Configurer();
    }

    public static void info(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
        alert.setHeaderText(header);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    public static void warning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content);
        alert.setHeaderText(header);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    public static void error(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content);
        alert.setHeaderText(header);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
}
