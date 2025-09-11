package practice.design.creational.prototype.solution.model;

public class Circle extends Shape {
    private int radius;

    public Circle() {
        super();
    }

    public Circle(Circle circle) {
        super(circle);
        this.radius = circle.radius;
        this.setStyle(circle.getStyle());
    }

    @Override
    public void draw() {
        System.out.println("Draw Circle at (" + getX() + "," + getY() + "), r=" + radius +
                ", color=" + getStyle().getColor() + ", stroke=" + getStyle().getStrokeWidth());
    }

    @Override
    public Circle duplicate() {
        return new Circle(this);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
