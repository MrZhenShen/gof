package practice.design.creational.factorySuccess.solution.factory;

import practice.design.creational.factorySuccess.solution.model.Document;
import practice.design.creational.factorySuccess.solution.model.ExcelDocument;

public class ExcelDocumentFactory implements DocumentFactory {

    @Override
    public Document create() {
        return new ExcelDocument();
    }
}
