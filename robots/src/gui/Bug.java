package gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Bug extends GameObject {

    public final double maxVelocity = 0.1;
    public final double maxAngularVelocity = 0.001;

    private double duration = 10;

    private Integer current;

    private Map<Integer, Point[]> levels = new HashMap<Integer, Point[]>() {
        {
            put(1, new Point[]{new Point(0, 960), new Point(0, 540)});
            put(2, new Point[]{new Point(960, 1920), new Point(0, 540)});
            put(3, new Point[]{new Point(0, 960), new Point(540, 1080)});
            put(4, new Point[]{new Point(960, 1920), new Point(540, 1080)});
        }
    };

    public Bug(double x, double y) {
        super(x, y, "bug_1.png", FieldCell.translateFactor);
        this.current = 1;
    }

    public void replaceBug(Integer level) {
        X_Position = levels.get(level)[0].x;
        Y_Position = levels.get(level)[1].x;
        this.current = level;
    }

    private double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private void move(double velocity, double angularVelocity) {

        double newX = X_Position + velocity * duration * Math.cos(Direction);
        double newY = Y_Position + velocity * duration * Math.sin(Direction);
        if (newX > levels.get(current)[0].x && newX < levels.get(current)[0].y &&
                newY > levels.get(current)[1].x && newY < levels.get(current)[1].y) {
            X_Position = newX;
            Y_Position = newY;
        }
        Direction = asNormalizedRadians(Direction + angularVelocity * duration * 4);
    }

    public void onModelUpdateEvent(double targetX, double targetY) {
        double dist = distance(targetX, targetY, X_Position, Y_Position);
        if (dist < 0.7) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(X_Position, Y_Position, targetX, targetY);
        double angularVelocity = 0;
        double angleBetweenTargetRobot = asNormalizedRadians(angleToTarget - Direction);
        if (angleBetweenTargetRobot < Math.PI) {
            angularVelocity = maxAngularVelocity;
        } else {
            angularVelocity = -maxAngularVelocity;
        }

        if (Math.abs(angleToTarget - Direction) < 0.05) {

            move(velocity, angularVelocity);
        } else {
            if (dist < 15) {
                move(0, angularVelocity);
            } else {
                move(velocity / 2, angularVelocity);
            }
        }

    }
}
