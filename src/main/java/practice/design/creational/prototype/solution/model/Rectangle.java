package practice.design.creational.prototype.solution.model;

public class Rectangle extends Shape {
    private int width;
    private int height;

    public Rectangle() {
        super();
    }

    public Rectangle(Rectangle rectangle) {
        super(rectangle);
        this.width = rectangle.width;
        this.height = rectangle.height;
        this.setStyle(rectangle.getStyle());
    }

    @Override
    void draw() {
        System.out.println("Draw Rect at (" + getX() + "," + getY() + "), " +
                width + "x" + height + ", color=" + getStyle().getColor() +
                ", stroke=" + getStyle().getStrokeWidth());
    }

    @Override
    Rectangle duplicate() {
        return new Rectangle(this);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
