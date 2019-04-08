package gui;

public class Portal extends GameObject{
    public Integer level;

    public Portal(double x, double y, Integer level){
        super(x, y, "portal.png", FieldCell.translateFactor);
        FieldCell cell = FieldCell.getCell(X_Position, Y_Position);
        X_Position = cell.X * FieldCell.translateFactor;
        Y_Position = cell.Y * FieldCell.translateFactor;
        Direction = - Math.PI / 2;
        Picture.setX(X_Position - Size / 2);
        Picture.setY(Y_Position - Size / 2);
        Picture.setRotate(Direction);
        this.level = level;
    }
}
