package practice.design.creational.builderFailed.solution.factory;

import practice.design.creational.builderFailed.solution.model.Report;

public class PdfReportFactory implements ReportFactory {

    @Override
    public Report generate(String title, String content) {
        return new Report()
                .addHeader("[PDF HEADER: " + title + "]")
                .addBody("[PDF BODY: " + content + "]")
                .addFooter("[PDF FOOTER]")
                ;
    }
}
