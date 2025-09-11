package practice.design.creational.factory.solution.factory;

import practice.design.creational.factory.solution.model.Document;
import practice.design.creational.factory.solution.model.PdfDocument;

public class PdfDocumentFactory implements DocumentFactory {

    @Override
    public Document create() {
        return new PdfDocument();
    }
}
