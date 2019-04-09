package gui;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import log.LogWindowSource;

import java.awt.*;

public class RobotsProgram extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setTitle("Robots");
        primaryStage.setHeight(size.height);
        primaryStage.setWidth(size.width);
        new MainApplicationStage(primaryStage, new LogWindowSource(5));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args);
    }
}
