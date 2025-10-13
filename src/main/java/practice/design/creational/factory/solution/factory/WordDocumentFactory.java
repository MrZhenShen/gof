package practice.design.creational.factory.solution.factory;

import practice.design.creational.factory.solution.model.Document;
import practice.design.creational.factory.solution.model.WordDocument;

public class WordDocumentFactory implements DocumentFactory {

    @Override
    public Document create() {
        return new WordDocument();
    }
}
