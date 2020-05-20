package ru.xander.swissknife;

import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Alexander Shakhov
 */
public class SwissKnifeStageReady extends ApplicationEvent {

    @Getter
    private final Stage stage;

    public SwissKnifeStageReady(Stage stage) {
        super(stage);
        this.stage = stage;
    }
}
