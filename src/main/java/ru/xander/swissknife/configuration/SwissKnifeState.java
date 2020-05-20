package ru.xander.swissknife.configuration;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Alexander Shakhov
 */
@Slf4j
@Component
public class SwissKnifeState {

    private static final String KEY_JAXB_WSDL_FILE = "jaxb.wsdl.file";
    private static final String KEY_JAXB_SOURCE_PATH = "jaxb.source.path";
    private static final String KEY_JAXB_TARGET_PACKAGE = "jaxb.target.package";

    private final File stateFile;

    private final StringProperty jaxbWsdlFile = new SimpleStringProperty("");
    private final StringProperty jaxbSourcePath = new SimpleStringProperty("");
    private final StringProperty jaxbTargetPackage = new SimpleStringProperty("");

    public SwissKnifeState(@Value("${spring.profiles.active:default}") String profile) {
        this.stateFile = new File(System.getProperty("user.dir"), "swiss-knife-" + profile + ".properties");
    }

    @PostConstruct
    protected void init() {
        if (stateFile.exists()) {
            log.info("Load application properties.");
            try (InputStream inputStream = new FileInputStream(stateFile)) {
                Properties properties = new Properties();
                properties.load(inputStream);
                jaxbWsdlFile.setValue(properties.getProperty(KEY_JAXB_WSDL_FILE));
                jaxbSourcePath.setValue(properties.getProperty(KEY_JAXB_SOURCE_PATH));
                jaxbTargetPackage.setValue(properties.getProperty(KEY_JAXB_TARGET_PACKAGE));
            } catch (Exception e) {
                log.warn("Cannot read properties from file {}", stateFile.getAbsolutePath(), e);
            }
        }
    }

    @PreDestroy
    protected void destroy() {
        log.info("Save application properties.");
        try (OutputStream outputStream = new FileOutputStream(stateFile)) {
            Properties properties = new Properties();
            properties.setProperty(KEY_JAXB_WSDL_FILE, jaxbWsdlFile.getValue());
            properties.setProperty(KEY_JAXB_SOURCE_PATH, jaxbSourcePath.getValue());
            properties.setProperty(KEY_JAXB_TARGET_PACKAGE, jaxbTargetPackage.getValue());
            properties.store(outputStream, null);
        } catch (Exception e) {
            log.warn("Cannot properties from file {}", stateFile.getAbsolutePath(), e);
        }
    }

    //region getters and setters

    public String getJaxbWsdlFile() {
        return jaxbWsdlFile.get();
    }

    public StringProperty jaxbWsdlFileProperty() {
        return jaxbWsdlFile;
    }

    public void setJaxbWsdlFile(String jaxbWsdlFile) {
        this.jaxbWsdlFile.set(jaxbWsdlFile);
    }

    public String getJaxbSourcePath() {
        return jaxbSourcePath.get();
    }

    public StringProperty jaxbSourcePathProperty() {
        return jaxbSourcePath;
    }

    public void setJaxbSourcePath(String jaxbSourcePath) {
        this.jaxbSourcePath.set(jaxbSourcePath);
    }

    public String getJaxbTargetPackage() {
        return jaxbTargetPackage.get();
    }

    public StringProperty jaxbTargetPackageProperty() {
        return jaxbTargetPackage;
    }

    public void setJaxbTargetPackage(String jaxbTargetPackage) {
        this.jaxbTargetPackage.set(jaxbTargetPackage);
    }


    //endregion
}
