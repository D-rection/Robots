package gui;

public class Target extends GameObject {

    public volatile double final_X_Position = 100;
    public volatile double final_Y_Position = 100;

    public void setTargetPosition(double x, double y) {
        X_Position = x;
        Y_Position = y;
    }

    public void setPromTargetPosition(double x, double y) {
        final_X_Position = x;
        final_Y_Position = y;
    }

    Target(double x, double y)
    {
        super(x, y, "apple.png", 30);
        Direction = - Math.PI / 2;
    }

    public void draw()
    {
        double drawX = final_X_Position - Size / 2;
        double drawY = final_Y_Position - Size / 2;
        Picture.setX(drawX);
        Picture.setY(drawY);
        Picture.setRotate(90 + Direction * 180 / Math.PI);
    }
}
