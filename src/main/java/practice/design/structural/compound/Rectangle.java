package practice.design.structural.compound;

public class Rectangle implements Graphic {
    private final int width;
    private final int height;

    public Rectangle(int width, int height) { this.width = width; this.height = height; }

    @Override public void draw() {
        System.out.println("Draw rectangle " + width + "x" + height);
    }
}
