package practice.design.creational.builderfailed.raw;

public class Main {
    public static void main(String[] args) {
        ReportGenerator generator = new ReportGenerator();

        Report textReport = generator.generate("text", "Sales Q1", "Some data...");
        System.out.println(textReport.getContent());

        Report htmlReport = generator.generate("html", "Sales Q1", "Some data...");
        System.out.println(htmlReport.getContent());
    }
}
