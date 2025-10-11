package practice.design.creational.builderfailed.solution.factory;

import practice.design.creational.builderfailed.solution.model.Report;

public interface ReportFactory {
    Report generate(String title, String content);
}
