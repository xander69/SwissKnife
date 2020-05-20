package ru.xander.swissknife.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Shakhov
 */
@Component
@FxmlView("/views/main.fxml")
public class MainController {

    @Setter
    private Scene scene;

    @FXML
    public void initialize() {
        // do nothing
    }
}
