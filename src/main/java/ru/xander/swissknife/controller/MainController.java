package ru.xander.swissknife.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xander.swissknife.configuration.SwissKnifeState;
import ru.xander.swissknife.controller.main.MainTabJaxb;
import ru.xander.swissknife.controller.main.MainTabProperties;
import ru.xander.swissknife.dto.JavaProperty;

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
    public TabPane tabPane;
    @FXML
    public ProgressIndicator backgroundIndicator;
    @FXML
    public Label backgroundLabel;

    //region tab java properties
    @FXML
    public Tab tabProperties;
    @FXML
    public TextField textJavaPropFilter;
    @FXML
    public TableView<JavaProperty> tableJavaProperties;
    @FXML
    public TableColumn<JavaProperty, String> columnPropName;
    @FXML
    public TableColumn<JavaProperty, String> columnPropValue;
    //endregion tab java properties

    //region tab jaxb generator
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
    //endregion tab jaxb generator

    @Autowired
    public MainController(SwissKnifeState state) {
        this.state = state;
    }

    @FXML
    public void initialize() {
        hideBackgroundIndicator();
        backgroundIndicator.setVisible(false);
        backgroundLabel.setVisible(false);
        tabPane.getSelectionModel().select(state.getActiveTab());
        state.activeTabProperty().bind(tabPane.getSelectionModel().selectedIndexProperty());
        MainTabProperties.initialize(this);
        MainTabJaxb.initialize(this);
    }

    public void showBackgroundIndicator() {
        backgroundIndicator.setVisible(true);
        backgroundLabel.setVisible(true);
    }

    public void hideBackgroundIndicator() {
        backgroundIndicator.setVisible(false);
        backgroundLabel.setVisible(false);
    }
}
