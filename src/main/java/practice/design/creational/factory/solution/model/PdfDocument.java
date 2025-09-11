package practice.design.creational.factory.solution.model;

public class PdfDocument implements Document {
    public void open() {
        System.out.println("Opening PDF document...");
    }
}
