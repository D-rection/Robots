package gui;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Field {
    private Set<FieldCell> badCells = new HashSet<>();
    Set<FieldCell> portalCells = new HashSet<>();

    private Bug bug;
    private Target target;

    private Map<Integer, Point> shifts = new HashMap<>();
    private Map<Integer, Integer> oneAfterAnother = new HashMap<Integer, Integer>()
    {
        {
            put(1, 2);
            put(2, 3);
            put(3, 4);
            put(4, 1);
        }
    };
    private GameField game;


    public Field(Bug bug, Target target, Wall[] walls, Mine[] mines,  Portal[] portals, GameField game)
    {
        this.game = game;
        this.bug = bug;
        this.target = target;
        for(Wall wall: walls){
            FieldCell cell = FieldCell.getCell(wall.X_Position, wall.Y_Position);
            badCells.add(cell);
        }
        for(Mine mine: mines){
            FieldCell cell = FieldCell.getCell(mine.X_Position, mine.Y_Position);
            badCells.add(cell);
        }
        for(Portal portal: portals){
            FieldCell cell = FieldCell.getCell(portal.X_Position, portal.Y_Position);
            portalCells.add(cell);
            shifts.put(portal.level, new Point((int)portal.X_Position, (int)portal.Y_Position));
        }
    }

    public void setTargetPosition(double x, double y)
    {
        FieldCell cell = FieldCell.getCell(x, y);
        if (!badCells.contains(cell))
            target.setTargetPosition(x, y);
        else {
            System.out.println("Bad Cell!" + cell.X + " " + cell.Y);
            System.out.println(x + " " + y);
        }
    }

    private boolean isSmash() { return badCells.contains(FieldCell.getCell(bug.X_Position, bug.Y_Position)); }

    private boolean isPortal()
    {
        return portalCells.contains(FieldCell.getCell(bug.X_Position, bug.Y_Position));
    }

    void onModelUpdateEvent(){
        if (game.currentField != game.destField){
            target.X_Position = shifts.get(game.currentField).x;
            target.Y_Position = shifts.get(game.currentField).y;

            bug.onModelUpdateEvent(target.X_Position, target.Y_Position);
            game.changeCurrentField(bug.X_Position, bug.Y_Position);
        }
        else {
            target.X_Position = target.final_X_Position;
            target.Y_Position = target.final_Y_Position;
            bug.onModelUpdateEvent(target.X_Position, target.Y_Position);
        }
            //game.changeCurrentField(bug.X_Position, bug.Y_Position, height, width);
        if (isSmash()) {
            System.out.println("Bug is dead...");
            System.exit(0);
        }
        if (isPortal()) {
            System.out.println("Yes");

            for (Integer el : shifts.keySet()) {
                if (FieldCell.getCell(shifts.get(el).x, shifts.get(el).y).equals(FieldCell.getCell(target.X_Position, target.Y_Position))) {
                    bug.replaceBug(oneAfterAnother.get(el));
                }
            }
            game.currentField = oneAfterAnother.get(game.currentField);
        }
    }

    void draw()
    {
        bug.draw();
        target.draw();
    }
}
