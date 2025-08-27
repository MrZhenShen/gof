package practice.design.creational.builderFailed.solution.factory;

import practice.design.creational.builderFailed.solution.model.Report;

public class HtmlReportFactory implements ReportFactory {
    @Override
    public Report generate(String title, String content) {
        return new Report()
                .addHeader("<h1>" + title + "</h1>")
                .addBody("<p>" + content + "</p>")
                .addFooter("<hr><i>End of report</i>")
                ;
    }
}
