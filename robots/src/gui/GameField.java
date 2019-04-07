package gui;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameField {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private Field field;

    private final Canvas canvas;
    private final Pane pane;
    private final ImageView background;


    public GameField(Pane pane) {
        this.pane = pane;
        background = loadFile("levels.jpg", this.pane.getWidth(), this.pane.getHeight());

        Target apple = new Target(150, 100);
        this.pane.getChildren().add(apple.Picture);

        Bug bug = new Bug(100, 100);
        this.pane.getChildren().add(bug.Picture);

        Mine[] mines = new Mine[]{new Mine(1100, 100), new Mine(500, 500), new Mine(800, 200),
                new Mine(600, 300), new Mine(800, 700), new Mine(100, 700)};
        for(Mine mine: mines){
            this.pane.getChildren().add(mine.Picture);
        }

        Portal[] portals = new Portal[]{new Portal(200, 200, 2), new Portal(1160, 200, 3),
                new Portal(200, 740, 4), new Portal(1160, 740, 1) };
        for(Portal portal: portals){
            this.pane.getChildren().add(portal.Picture);
        }

        Wall[] walls = new Wall[]{new Wall(250, 600), new Wall(300, 600), new Wall(350, 600),
                new Wall(350, 650), new Wall(800, 250), new Wall(850, 250), new Wall(900, 250),
                new Wall(900, 300),  new Wall(900, 350),  new Wall(900, 400), new Wall(400, 100),
                new Wall(450, 100), new Wall(500, 100),};
        for(Wall wall: walls){
            this.pane.getChildren().add(wall.Picture);
        }

        field = new Field(bug, apple, walls, mines, portals);

        canvas = new Canvas();
        this.pane.getChildren().add(canvas);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                field.onModelUpdateEvent();
            }
        }, 0, 5);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                paint();
            }
        }, 0, 20);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                field.setTargetPosition(e.getX(), e.getY());
            }
        });
    }

    private ImageView loadFile(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        ImageView picture = new ImageView(image);
        this.pane.getChildren().add(picture);
        return picture;
    }

    private void paint() {
        background.setFitHeight(pane.getHeight());
        background.setFitWidth(pane.getWidth());
        canvas.setHeight(pane.getHeight());
        canvas.setWidth(pane.getWidth());
        field.draw();
    }
}