package practice.design.creational.prototype.raw;

public class Rectangle extends Shape {
    public int width;
    public int height;

    public Rectangle(int x, int y, int width, int height, Style style) {
        super(x, y, style);
        this.width = width;
        this.height = height;
    }

    @Override
    void draw() {
        System.out.println("Draw Rect at (" + x + "," + y + "), " +
                width + "x" + height + ", color=" + style.color +
                ", stroke=" + style.strokeWidth);
    }
}
