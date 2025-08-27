package practice.design.creational.builderFailed.solution.factory;

import practice.design.creational.builderFailed.solution.model.Report;

public class TextReportFactory implements ReportFactory {

    @Override
    public Report generate(String title, String content) {
        return new Report()
                .addHeader("=== " + title + " ===")
                .addBody(content)
                .addFooter("--- end of report ---")
                ;
    }
}
