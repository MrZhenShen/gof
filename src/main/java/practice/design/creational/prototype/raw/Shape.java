package practice.design.creational.prototype.raw;

public abstract class Shape {
    public int x;
    public int y;
    public Style style;

    Shape(int x, int y, Style style) {
        this.x = x;
        this.y = y;
        this.style = style;
    }

    abstract void draw();
}
