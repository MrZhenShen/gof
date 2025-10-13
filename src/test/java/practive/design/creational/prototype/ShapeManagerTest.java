package practive.design.creational.prototype;

import org.junit.jupiter.api.Test;
import practice.design.creational.prototype.raw.Circle;
import practice.design.creational.prototype.raw.Rectangle;
import practice.design.creational.prototype.raw.ShapeManager;
import practice.design.creational.prototype.raw.Style;

import static org.junit.jupiter.api.Assertions.*;

class ShapeManagerTest {

    @Test
    void duplicateCircle_copiesValueFields_butSharesStyleReference() {
        Style style = new Style("red", 2.0f);
        Circle original = new Circle(10, 10, 20, style);

        Circle copy = new ShapeManager().duplicateCircle(original);

        // Value fields are copied
        assertNotSame(original, copy);
        assertEquals(10, copy.x);
        assertEquals(10, copy.y);
        assertEquals(20, copy.radius);

        // 🔴 BUG: Style reference is shared (shallow copy)
        assertSame(original.style, copy.style, "Expected shared Style reference (shallow copy)");
        assertEquals("red", copy.style.color);
        assertEquals(2.0f, copy.style.strokeWidth);
    }

    @Test
    void duplicateCircle_modifyingCopyStyleAffectsOriginal() {
        Style style = new Style("red", 2.0f);
        Circle original = new Circle(0, 0, 5, style);

        Circle copy = new ShapeManager().duplicateCircle(original);
        copy.style.color = "blue";
        copy.style.strokeWidth = 5.0f;

        // 🔴 BUG: Changing copy's style also changes original's style
        assertEquals("blue", original.style.color);
        assertEquals(5.0f, original.style.strokeWidth);
    }

    @Test
    void duplicateRectangle_copiesValueFields_butSharesStyleReference() {
        Style style = new Style("green", 1.5f);
        Rectangle original = new Rectangle(5, 7, 30, 40, style);

        Rectangle copy = new ShapeManager().duplicateRectangle(original);

        // Value fields are copied
        assertNotSame(original, copy);
        assertEquals(5, copy.x);
        assertEquals(7, copy.y);
        assertEquals(30, copy.width);
        assertEquals(40, copy.height);

        // 🔴 BUG: Style reference is shared
        assertSame(original.style, copy.style, "Expected shared Style reference (shallow copy)");
        assertEquals("green", copy.style.color);
        assertEquals(1.5f, copy.style.strokeWidth);
    }

    @Test
    void duplicateRectangle_modifyingCopyStyleAffectsOriginal() {
        Style style = new Style("black", 3.0f);
        Rectangle original = new Rectangle(0, 0, 10, 10, style);

        Rectangle copy = new ShapeManager().duplicateRectangle(original);
        copy.style.color = "yellow";
        copy.style.strokeWidth = 0.5f;

        // 🔴 BUG: Changing copy's style also changes original's style
        assertEquals("yellow", original.style.color);
        assertEquals(0.5f, original.style.strokeWidth);
    }
}
