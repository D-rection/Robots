package gui;

import java.awt.*;

import javafx.application.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

public class TestLevel {
    private volatile boolean success = false;
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    double height = size.height;
    double width = size.width;


    @Test
    public void testJavaFXApplicationLaunch() {
        Thread thread = new Thread() { // Wrapper thread.
            @Override
            public void run() {
                try
                {
                    Application.launch(RobotsProgram.class); // Run JavaFX application.
                    success = true;
                } catch(Throwable t) {
                    if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                        success = true;
                        return;
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);
        } catch(InterruptedException ex) {
        }
        thread.interrupt();
        try {
            thread.join(1);
        } catch(InterruptedException ex) {
        }
        assertTrue(success);
    }

    @Test
    public void replaceLevelTest()
    {
        Bug bug = new Bug(500, 500);
        bug.replaceBug(2);
        assertEquals(2, bug.current);
        assertEquals(width/2+1, bug.X_Position);
        assertEquals(0, bug.Y_Position);
    }

    @Test
    public void testChangePortal() {
        Thread thread = new Thread() { // Wrapper thread.
            @Override
            public void run() {
                try {
                    Application.launch(RobotsProgram.class); // Run JavaFX application.

                } catch (Throwable t) {
                    if (t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                        return;
                    }
                }
            }
        };

        thread.setDaemon(true);
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        Portal[] portals = new Portal[]{new Portal((GameWindow.width / 8) * 3, GameWindow.height / 8, 1)};
        Bug bug = new Bug((GameWindow.width / 8) * 3, GameWindow.height / 8);
        Field field = new Field(bug, new Target(100, 500), new Wall[0], new Mine[0], portals, new GameField(new Pane()));
        assertTrue(field.isPortal());


        Pane pane = new GameWindow(height, width);
        GameField game = new GameField(pane);
        game.currentField = 1;
        game.destField = 2;
        game.changeCurrentField(bug.X_Position, bug.Y_Position);
        assertEquals(1, game.currentField);

        thread.interrupt();
        try {
            thread.join(1);}
        catch (InterruptedException ex)
        { }
    }
}


