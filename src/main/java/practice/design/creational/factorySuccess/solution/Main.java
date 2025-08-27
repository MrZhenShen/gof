package practice.design.creational.factorySuccess.solution;

import practice.design.creational.factorySuccess.solution.factory.DocumentFactory;
import practice.design.creational.factorySuccess.solution.factory.ExcelDocumentFactory;
import practice.design.creational.factorySuccess.solution.factory.PdfDocumentFactory;
import practice.design.creational.factorySuccess.solution.model.Document;

public class Main {

    public static void main(String[] args) {
        DocumentFactory excelDocumentFactory = new ExcelDocumentFactory();

        Document excel = excelDocumentFactory.create();
        excel.open();

        DocumentFactory pdfDocumentFactory = new PdfDocumentFactory();

        Document pdf = pdfDocumentFactory.create();
        pdf.open();
    }
}
