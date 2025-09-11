package practice.design.creational.prototype.solution;

import practice.design.creational.prototype.solution.model.Circle;
import practice.design.creational.prototype.solution.model.Style;

public class Main {
    public static void main(String[] args) {

        Circle original = new Circle();
        original.setX(10);
        original.setY(10);
        original.setRadius(20);
        original.setStyle(new Style("red", 2.0f));

        Circle copy = original.duplicate();

        original.draw();
        copy.draw();

        copy.setStyle(new Style("blue", 5.0f));

        System.out.println("-- after copy style change --");

        original.draw();
        copy.draw();
    }
}
