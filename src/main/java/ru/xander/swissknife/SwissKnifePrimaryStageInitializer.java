package ru.xander.swissknife;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.xander.swissknife.controller.MainController;

/**
 * @author Alexander Shakhov
 */
@Component
public class SwissKnifePrimaryStageInitializer implements ApplicationListener<SwissKnifeStageReady> {

    private static final double MIN_WIDTH = 1240.0;
    private static final double MIN_HEIGHT = 780.0;
    private final FxWeaver fxWeaver;

    @Autowired
    public SwissKnifePrimaryStageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(SwissKnifeStageReady event) {
        Stage stage = event.getStage();
        stage.setTitle("Swiss Knife");
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        FxControllerAndView<MainController, Node> controllerAndView = fxWeaver.load(MainController.class);
        controllerAndView.getView().ifPresent(parent -> {
            Scene scene = new Scene((Parent) parent, MIN_WIDTH, MIN_HEIGHT);
            stage.setScene(scene);
            controllerAndView.getController().setScene(scene);
        });

        stage.show();
    }
}
