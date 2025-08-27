package practice.design.creational.builderFailed.solution;

import practice.design.creational.builderFailed.solution.factory.HtmlReportFactory;
import practice.design.creational.builderFailed.solution.factory.ReportFactory;
import practice.design.creational.builderFailed.solution.factory.TextReportFactory;
import practice.design.creational.builderFailed.solution.model.Report;

public class Main {
    public static void main(String[] args) {

        ReportFactory textReportFactory = new TextReportFactory();

        Report textReport = textReportFactory.generate("Sales Q1", "Some data...");
        System.out.println(textReport.getContent());

        ReportFactory htmlReportFactory = new HtmlReportFactory();
        Report htmlReport = htmlReportFactory.generate("Sales Q1", "Some data...");
        System.out.println(htmlReport.getContent());
    }
}
