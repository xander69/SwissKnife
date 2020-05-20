package ru.xander.swissknife.util;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lombok.Getter;

/**
 * @author Alexander Shakhov
 */
@Getter
public class MessageDialog extends Dialog {

    public MessageDialog(Window owner,
                         String title,
                         String content,
                         boolean editable,
                         double width,
                         double height,
                         Font font) {
        Scene scene = new Scene(dialogPane);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(owner);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setMinWidth(width);
        stage.setMinHeight(height);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(width);
        textArea.setPrefHeight(height);
        textArea.setText(content);
        textArea.setEditable(editable);
        if (font != null) {
            textArea.setFont(font);
        }

        AnchorPane.setLeftAnchor(textArea, 0.0);
        AnchorPane.setTopAnchor(textArea, 0.0);
        AnchorPane.setRightAnchor(textArea, 0.0);
        AnchorPane.setBottomAnchor(textArea, 0.0);

        dialogPane.getChildren().addAll(textArea);
    }

    public static class Configurer {

        private Window owner;
        private String title = "Message";
        private String content;
        private double width = 600.0;
        private double height = 400.0;
        private boolean editable = false;
        private Font font;

        public Configurer owner(Window owner) {
            this.owner = owner;
            return this;
        }

        public Configurer title(String title) {
            this.title = title;
            return this;
        }

        public Configurer content(String content) {
            this.content = content;
            return this;
        }

        public Configurer width(double width) {
            this.width = width;
            return this;
        }

        public Configurer height(double height) {
            this.height = height;
            return this;
        }

        public Configurer editable(boolean editable) {
            this.editable = editable;
            return this;
        }

        public Configurer font(Font font) {
            this.font = font;
            return this;
        }

        public void show() {
            new MessageDialog(owner, title, content, editable, width, height, font).showAndWait();
        }
    }
}
