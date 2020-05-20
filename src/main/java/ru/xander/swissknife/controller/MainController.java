package ru.xander.swissknife.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.xander.swissknife.controller.main.MainTabJaxb;

/**
 * @author Alexander Shakhov
 */
@Component
@FxmlView("/views/main.fxml")
public class MainController {
    @Setter
    public Scene scene;

    @FXML
    public Tab tabJaxb;
    @FXML
    public TextField textJaxbWsdlPath;
    @FXML
    public Button buttonChooseWsdlPath;
    @FXML
    public TextField textJaxbSrcPath;
    @FXML
    public Button buttonChooseSrcPath;
    @FXML
    public Button buttonJaxbGenerate;
    @FXML
    public TextArea textJaxbLog;

    @FXML
    public void initialize() {
        MainTabJaxb.initialize(this);
    }
}
