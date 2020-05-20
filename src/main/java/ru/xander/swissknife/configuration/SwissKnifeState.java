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

    private static final String KEY_JAXB_WSDL_PATH = "jaxb.wsdl.path";
    private static final String KEY_JAXB_SRC_PATH = "jaxb.src.path";

    private final File stateFile;

    private final StringProperty jaxbWsdlPath = new SimpleStringProperty("");
    private final StringProperty jaxbSrcPath = new SimpleStringProperty("");

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
                jaxbWsdlPath.setValue(properties.getProperty(KEY_JAXB_WSDL_PATH));
                jaxbSrcPath.setValue(properties.getProperty(KEY_JAXB_SRC_PATH));
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
            properties.setProperty(KEY_JAXB_WSDL_PATH, jaxbWsdlPath.getValue());
            properties.setProperty(KEY_JAXB_SRC_PATH, jaxbSrcPath.getValue());
            properties.store(outputStream, null);
        } catch (Exception e) {
            log.warn("Cannot properties from file {}", stateFile.getAbsolutePath(), e);
        }
    }

    //region getters and setters

    public String getJaxbWsdlPath() {
        return jaxbWsdlPath.get();
    }

    public StringProperty jaxbWsdlPathProperty() {
        return jaxbWsdlPath;
    }

    public void setJaxbWsdlPath(String jaxbWsdlPath) {
        this.jaxbWsdlPath.set(jaxbWsdlPath);
    }

    public String getJaxbSrcPath() {
        return jaxbSrcPath.get();
    }

    public StringProperty jaxbSrcPathProperty() {
        return jaxbSrcPath;
    }

    public void setJaxbSrcPath(String jaxbSrcPath) {
        this.jaxbSrcPath.set(jaxbSrcPath);
    }

    //endregion
}
