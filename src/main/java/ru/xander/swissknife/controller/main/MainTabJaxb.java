package ru.xander.swissknife.controller.main;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.util.StringUtils;
import ru.xander.swissknife.controller.MainController;

import java.io.File;

/**
 * @author Alexander Shakhov
 */
@SuppressWarnings("WeakerAccess")
public class MainTabJaxb {

    private final MainController main;

    private MainTabJaxb(MainController main) {
        this.main = main;
    }

    private void initializeTab() {
        main.textJaxbWsdlPath.textProperty().bindBidirectional(main.state.jaxbWsdlPathProperty());
        main.textJaxbSrcPath.textProperty().bindBidirectional(main.state.jaxbSrcPathProperty());
        main.buttonChooseWsdlPath.setOnAction(event -> chooseWsdlPath());
        main.buttonChooseSrcPath.setOnAction(event -> chooseSourcePath());
    }

    public static void initialize(MainController main) {
        new MainTabJaxb(main).initializeTab();
    }

    public void chooseWsdlPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WSDL and XSD", "*.wsdl;*.xsd"),
                new FileChooser.ExtensionFilter("WSDL", "*.wsdl"),
                new FileChooser.ExtensionFilter("XSD", "*.xsd")
        );
        String currentWsdlFile = main.textJaxbWsdlPath.getText();
        String currentSourcePath = main.textJaxbSrcPath.getText();
        if (!StringUtils.isEmpty(currentWsdlFile)) {
            File initialFile = new File(currentWsdlFile);
            if (initialFile.getParentFile().exists()) {
                fileChooser.setInitialDirectory(initialFile.getParentFile());
            }
        } else if (!StringUtils.isEmpty(currentSourcePath)) {
            File initialDirectory = new File(currentSourcePath);
            if (initialDirectory.exists()) {
                fileChooser.setInitialDirectory(initialDirectory);
            }
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }
        File wsdlFile = fileChooser.showOpenDialog(main.scene.getWindow());
        if (wsdlFile != null) {
            main.textJaxbWsdlPath.setText(wsdlFile.getAbsolutePath());
        }
    }

    public void chooseSourcePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        String currentWsdlFile = main.textJaxbWsdlPath.getText();
        String currentSourcePath = main.textJaxbSrcPath.getText();
        if (!StringUtils.isEmpty(currentSourcePath)) {
            File initialDirectory = new File(currentSourcePath);
            if (initialDirectory.exists()) {
                directoryChooser.setInitialDirectory(initialDirectory);
            }
        } else if (!StringUtils.isEmpty(currentWsdlFile)) {
            File initialFile = new File(currentWsdlFile);
            if (initialFile.getParentFile().exists()) {
                directoryChooser.setInitialDirectory(initialFile.getParentFile());
            }
        } else {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        }
        File sourcePath = directoryChooser.showDialog(main.scene.getWindow());
        if (sourcePath != null) {
            main.textJaxbSrcPath.setText(sourcePath.getAbsolutePath());
        }
    }
}