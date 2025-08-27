package practice.design.creational.factorySuccess.solution.model;

public class PdfDocument implements Document {
    public void open() {
        System.out.println("Opening PDF document...");
    }
}
