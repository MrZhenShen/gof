package practice.design.creational.factory.solution.factory;

import practice.design.creational.factory.solution.model.Document;
import practice.design.creational.factory.solution.model.ExcelDocument;

public class ExcelDocumentFactory implements DocumentFactory {

    @Override
    public Document create() {
        return new ExcelDocument();
    }
}
