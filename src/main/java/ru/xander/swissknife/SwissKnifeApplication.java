package ru.xander.swissknife;

import javafx.application.Application;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author Alexander Shakhov
 */
@SpringBootApplication
public class SwissKnifeApplication {
    public static void main(String[] args) {
        Application.launch(SwissKnifeFxApplication.class, args);
    }

    @Bean
    public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
        return new SpringFxWeaver(applicationContext);
    }
}
