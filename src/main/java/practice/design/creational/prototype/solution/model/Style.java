package practice.design.creational.prototype.solution.model;

public class Style {

    private String color;
    private float strokeWidth;

    public Style() {
        color = "gray";
        strokeWidth = 1;
    }

    public Style(Style style) {
        color = style.color;
        strokeWidth = style.strokeWidth;
    }

    public Style(String color, float strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public Style setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public String getColor() {
        return color;
    }

    public Style setColor(String color) {
        this.color = color;
        return this;
    }
}
