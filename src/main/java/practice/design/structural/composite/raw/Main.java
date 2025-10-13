package practice.design.structural.composite.raw;

import java.util.ArrayList;
import java.util.List;

interface Graphic {
    void draw();
}

// Прості об’єкти
class Line implements Graphic {
    private final int length;
    public Line(int length) { this.length = length; }
    @Override public void draw() {
        System.out.println("Draw line of length " + length);
    }
}

class Rectangle implements Graphic {
    private final int w, h;
    public Rectangle(int w, int h) { this.w = w; this.h = h; }
    @Override public void draw() {
        System.out.println("Draw rectangle " + w + "x" + h);
    }
}

// Складний об’єкт
class Diagram {
    private final List<Graphic> elements = new ArrayList<>();
    public void add(Graphic g) { elements.add(g); }
    public void render() {
        for (Graphic g : elements) {
            g.draw();
        }
    }
}

// Клієнтський код
public class Main {
    public static void main(String[] args) {
        Line line = new Line(10);
        Rectangle rect = new Rectangle(4, 6);

        Diagram diagram = new Diagram();
        diagram.add(line);
        diagram.add(rect);

        // ❌ клієнт працює з різними типами по-різному
        line.draw();
        rect.draw();
        diagram.render();
    }
}
