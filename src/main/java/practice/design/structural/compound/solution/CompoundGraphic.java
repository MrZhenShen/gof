package practice.design.structural.compound.solution;

import java.util.ArrayList;
import java.util.List;

public class CompoundGraphic implements Graphic {
    protected List<Graphic> children;

    public CompoundGraphic(Graphic... children) {
        this.children = new ArrayList<>(List.of(children));
    }

    @Override
    public void draw() {
        children.forEach(Graphic::draw);
    }

    public void addGraphic(Graphic g) {
        children.add(g);
    }
}
