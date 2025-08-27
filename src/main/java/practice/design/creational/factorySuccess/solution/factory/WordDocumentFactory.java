package practice.design.creational.factorySuccess.solution.factory;

import practice.design.creational.factorySuccess.solution.model.Document;
import practice.design.creational.factorySuccess.solution.model.WordDocument;

public class WordDocumentFactory implements DocumentFactory {

    @Override
    public Document create() {
        return new WordDocument();
    }
}
