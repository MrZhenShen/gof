package practice.design.creational.factorySuccess.raw;

public class DocumentManager {
    public Document createDocument(String type) {
        if (type.equalsIgnoreCase("word")) {
            return new WordDocument();
        } else if (type.equalsIgnoreCase("pdf")) {
            return new PdfDocument();
        } else if (type.equalsIgnoreCase("excel")) {
            return new ExcelDocument();
        }
        throw new IllegalArgumentException("Unknown document type: " + type);
    }
}
