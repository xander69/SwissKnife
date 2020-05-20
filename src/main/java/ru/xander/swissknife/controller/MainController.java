package ru.xander.swissknife.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xander.swissknife.configuration.SwissKnifeState;
import ru.xander.swissknife.controller.main.MainTabJaxb;

/**
 * @author Alexander Shakhov
 */
@Component
@FxmlView("/views/main.fxml")
public class MainController {
    @Setter
    public Scene scene;

    public final SwissKnifeState state;

    @FXML
    public Tab tabJaxb;
    @FXML
    public TextField textJaxbWsdlFile;
    @FXML
    public TextField textJaxbSourcePath;
    @FXML
    public TextField textJaxbTargetPackage;
    @FXML
    public Button buttonChooseWsdlFile;
    @FXML
    public Button buttonChooseSourcePath;
    @FXML
    public Button buttonJaxbGenerate;
    @FXML
    public Button buttonWsImportHelp;
    @FXML
    public TextArea textJaxbLog;

    @Autowired
    public MainController(SwissKnifeState state) {
        this.state = state;
    }

    @FXML
    public void initialize() {
        MainTabJaxb.initialize(this);
    }
}
