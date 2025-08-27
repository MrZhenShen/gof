package practice.design.creational.builderFailed.solution.factory;

import practice.design.creational.builderFailed.solution.model.Report;

public interface ReportFactory {
    Report generate(String title, String content);
}
