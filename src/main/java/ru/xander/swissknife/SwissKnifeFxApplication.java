package ru.xander.swissknife;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Alexander Shakhov
 */
public class SwissKnifeFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(SwissKnifeApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.applicationContext.publishEvent(new SwissKnifeStageReady(primaryStage));
    }

    @Override
    public void stop() throws Exception {
        this.applicationContext.close();
        Platform.exit();
    }
}
