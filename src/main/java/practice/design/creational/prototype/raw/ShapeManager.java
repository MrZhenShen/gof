package practice.design.creational.prototype.raw;

public class ShapeManager {

    public Circle duplicateCircle(Circle src) {
        return new Circle(src.x, src.y, src.radius, src.style);
    }

    public Rectangle duplicateRectangle(Rectangle src) {
        return new Rectangle(src.x, src.y, src.width, src.height, src.style);
    }
}
