package gui;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
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

import static oracle.jrockit.jfr.events.Bits.intValue;

public class GameField {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public Field field;

    private final Canvas canvas;
    private final Pane pane;
    private final ImageView background;
    public Integer destField = 1;
    public Integer currentField = 1;

    static Map<Integer, Point[]> levels = new HashMap<Integer, Point[]>() {
        {
            put(1, new Point[]{
                    new Point(0, intValue(GameWindow.width/2)),
                    new Point(0, intValue(GameWindow.height/2))});
            put(2, new Point[]{
                    new Point(intValue(GameWindow.width/2+1), intValue(GameWindow.width)),
                    new Point(0, intValue(GameWindow.height/2))});
            put(3, new Point[]{
                    new Point(0, intValue(GameWindow.width/2)),
                    new Point(intValue(GameWindow.height/2+1), intValue(GameWindow.height))});
            put(4, new Point[]{
                    new Point(intValue(GameWindow.width/2+1), intValue(GameWindow.width)),
                    new Point(intValue(GameWindow.height/2+1), intValue(GameWindow.height))});
        }
    };


    public GameField(Pane pane) {
        this.pane = pane;
        background = loadFile("levels.png", this.pane.getWidth(), this.pane.getHeight());

        Target apple = new Target(150, 100);
        this.pane.getChildren().add(apple.Picture);

        Bug bug = new Bug(100, 100);
        this.pane.getChildren().add(bug.Picture);

        Mine[] mines = new Mine[]{
                new Mine(GameWindow.width/8, (GameWindow.height/8)*3),
                new Mine((GameWindow.width/10)*4, (GameWindow.height/10)*3),
                new Mine((GameWindow.width/8)*5, (GameWindow.height/10)*2),
                new Mine((GameWindow.width/10)*8, (GameWindow.height/10)*4),
                new Mine((GameWindow.width/10)*2, (GameWindow.height/10)*7),
                new Mine((GameWindow.width/10)*6, (GameWindow.height/8)*7),
                new Mine((GameWindow.width/10)*9, (GameWindow.height/10)*7)};
        for(Mine mine: mines){
            this.pane.getChildren().add(mine.Picture);
        }

        Portal[] portals = new Portal[]{
                new Portal((GameWindow.width/8)*3, GameWindow.height/8, 1),
                new Portal((GameWindow.width/8)*7, GameWindow.height/8, 2),
                new Portal((GameWindow.width/8)*3, (GameWindow.height/8)*5, 3),
                new Portal((GameWindow.width/8)*7, (GameWindow.height/8)*5, 4) };
        for(Portal portal: portals){
            this.pane.getChildren().add(portal.Picture);
        }

        Wall[] walls = new Wall[]{
                new Wall(GameWindow.width/5, GameWindow.height/5),
                new Wall(GameWindow.width/5, GameWindow.height/5+50),
                new Wall(GameWindow.width/4, GameWindow.height/4*3),
                new Wall(GameWindow.width/4+50, GameWindow.height/4*3),
                new Wall(GameWindow.width/4+100, GameWindow.height/4*3),
                new Wall(GameWindow.width/4*3, GameWindow.height/6*3-10),
                new Wall(GameWindow.width/4*3+50, GameWindow.height/6*3-10),
                new Wall(GameWindow.width/5*4+50, GameWindow.height/5+50),
                new Wall(GameWindow.width/5*4+100, GameWindow.height/5),
                new Wall(GameWindow.width/5*4+100, GameWindow.height/5+50)};
        for(Wall wall: walls){
            this.pane.getChildren().add(wall.Picture);
        }

        field = new Field(bug, apple, walls, mines, portals,this);

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
        }, 0, 25);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                FieldCell cell = FieldCell.getCell(e.getX(), e.getY());
                if(field.portalCells.contains(cell))
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация");
                    alert.setHeaderText(null);
                    alert.setContentText("Яблоко пропало в портале!");
                    alert.showAndWait();
                }
                else {
                    field.setTargetPosition(e.getX(), e.getY());
                    apple.setPromTargetPosition(e.getX(), e.getY());
                    changeDestField(e.getX(), e.getY());
                }
            }
        });
    }

    private void changeDestField(double positionX, double positionY){

        for (Map.Entry<Integer, Point[]> level: levels.entrySet())
        {
            if (positionX>=level.getValue()[0].x && positionX<=level.getValue()[0].y &&
                    positionY >= level.getValue()[1].x && positionY <=level.getValue()[1].y)
                destField = level.getKey();
        }
    }

    void changeCurrentField(double positionX, double positionY){

        for (Map.Entry<Integer, Point[]> level: levels.entrySet())
        {
            if (positionX>=level.getValue()[0].x && positionX<=level.getValue()[0].y &&
                    positionY >= level.getValue()[1].x && positionY <=level.getValue()[1].y)
                currentField = level.getKey();
        }
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