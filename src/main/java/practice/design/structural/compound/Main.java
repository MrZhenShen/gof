package practice.design.structural.compound;

public class Main {

    public static void main(String[] args) {
        CompoundGraphic diagram = new CompoundGraphic(new Line(10));
        diagram.addGraphic(new Rectangle(4, 6));

        diagram.draw();
    }
}
