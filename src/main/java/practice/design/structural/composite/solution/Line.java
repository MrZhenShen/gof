package practice.design.structural.composite.solution;

public class Line implements Graphic {
    private final int length;

    public Line(int length) { this.length = length; }

    @Override
    public void draw() {
        System.out.println("Draw line of length " + length);
    }
}
