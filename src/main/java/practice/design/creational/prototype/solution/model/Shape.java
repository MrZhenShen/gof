package practice.design.creational.prototype.solution.model;

public abstract class Shape {
    private int x;
    private int y;
    private Style style;

    Shape() {}

    Shape(Shape shape) {
        this.x = shape.x;
        this.y = shape.y;
        this.style = shape.style;
    }

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

    Style getStyle() {
        return new Style(style);
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    abstract void draw();
    abstract Shape duplicate();
}
