package practice.design.creational.factorySuccess.solution.factory;

import practice.design.creational.factorySuccess.solution.model.Document;
import practice.design.creational.factorySuccess.solution.model.PdfDocument;

public class PdfDocumentFactory implements DocumentFactory {

    @Override
    public Document create() {
        return new PdfDocument();
    }
}
