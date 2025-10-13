package practice.design.creational.builderfailed.solution.model;

import java.util.EnumMap;
import java.util.Map;

import practice.design.creational.builderfailed.solution.enums.ReportPart;

public class Report {
    private final Map<ReportPart, String> content;

    public Report() {
        content = new EnumMap<>(ReportPart.class);
    }

    public Report addHeader(String value) {
        content.put(ReportPart.HEADER, value);
        return this;
    }

    public Report addBody(String value) {
        content.put(ReportPart.BODY, value);
        return this;
    }

    public Report addFooter(String value) {
        content.put(ReportPart.FOOTER, value);
        return this;
    }

    public String getContent() {
        String[] contentSequence = {content.get(ReportPart.HEADER), content.get(ReportPart.BODY), content.get(ReportPart.FOOTER)};
        return String.join("\n", contentSequence);
    }
}

