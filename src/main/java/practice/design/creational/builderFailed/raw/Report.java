package practice.design.creational.builderfailed.raw;

public class Report {
    private String header;
    private String body;
    private String footer;

    public Report(String header, String body, String footer) {
        this.header = header;
        this.body = body;
        this.footer = footer;
    }

    public String getContent() {
        return header + "\n" + body + "\n" + footer;
    }
}
