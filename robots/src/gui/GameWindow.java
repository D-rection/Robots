package gui;

import javafx.scene.layout.Pane;

class GameWindow extends Pane {

    private final GameField gameVisualizer;
    static double height;
    static double width;

    GameWindow(double height, double width)
    {
        GameWindow.height = height;
        GameWindow.width = width;
        this.setHeight(GameWindow.height);
        this.setWidth(GameWindow.width);
        gameVisualizer = new GameField(this);
    }
}
