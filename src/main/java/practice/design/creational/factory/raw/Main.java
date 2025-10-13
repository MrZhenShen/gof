package practice.design.creational.factory.raw;

public class Main {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        Document doc1 = manager.createDocument("word");
        doc1.open();

        Document doc2 = manager.createDocument("pdf");
        doc2.open();
    }
}
