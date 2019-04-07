package gui;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Field {
    private Set<FieldCell> badCells = new HashSet<>();
    private Set<FieldCell> portalCells = new HashSet<>();

    private Bug bug;
    private Target target;

    private Map<Integer, Point> shifts = new HashMap<>();

    public Field(Bug bug, Target target, Wall[] walls, Mine[] mines,  Portal[] portals)
    {
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
            shifts.put(portal.nextLevel, new Point((int)portal.X_Position, (int)portal.Y_Position));
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

    public void onModelUpdateEvent(){
        bug.onModelUpdateEvent(target.X_Position, target.Y_Position);
        if (isSmash()) {
            System.out.println("Bug is dead...");
            System.exit(0);
        }
        if (isPortal()){
            for (Integer el: shifts.keySet()){
                if (FieldCell.getCell(shifts.get(el).x, shifts.get(el).y).equals(FieldCell.getCell(target.X_Position, target.Y_Position))){
                    bug.replaceBug(el);
                }
            }
        }
    }

    public void draw()
    {
        bug.draw();
        target.draw();
    }
}
