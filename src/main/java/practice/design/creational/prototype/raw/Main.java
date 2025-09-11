package practice.design.creational.prototype.raw;

public class Main {
    public static void main(String[] args) {
        Style style = new Style("red", 2.0f);

        Circle original = new Circle(10, 10, 20, style);
        Circle copy = new ShapeManager().duplicateCircle(original);

        original.draw();
        copy.draw();

        copy.style.color = "blue";
        copy.style.strokeWidth = 5.0f;

        System.out.println("-- after copy style change --");
        original.draw();
        copy.draw();
    }
}

