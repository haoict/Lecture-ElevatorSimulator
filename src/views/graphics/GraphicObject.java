package views.graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Class abstract parent of FixedObject and AnimatedObject A GraphicObject
 * object possesses two attributes x and y A non-abstract class that inherits
 * from an implementer must GraphicObject method dessineToi
 *
 * @author remy
 *
 */
public abstract class GraphicObject {

    protected int x, y;
    protected Color color;

    public GraphicObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void drawYourself(Graphics g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
