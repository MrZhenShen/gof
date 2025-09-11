package practice.design.creational.prototype.raw;

public class Circle extends Shape {
    public int radius;

    public Circle(int x, int y, int radius, Style style) {
        super(x, y, style);
        this.radius = radius;
    }

    @Override
    void draw() {
        System.out.println("Draw Circle at (" + x + "," + y + "), r=" + radius +
                ", color=" + style.color + ", stroke=" + style.strokeWidth);
    }
}
