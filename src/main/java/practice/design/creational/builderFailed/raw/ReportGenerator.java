package practice.design.creational.builderFailed.raw;

public class ReportGenerator {
    public Report generate(String type, String title, String content) {
        if (type.equalsIgnoreCase("text")) {
            return new Report("=== " + title + " ===",
                    content,
                    "--- end of report ---");
        } else if (type.equalsIgnoreCase("html")) {
            return new Report("<h1>" + title + "</h1>",
                    "<p>" + content + "</p>",
                    "<hr><i>End of report</i>");
        } else if (type.equalsIgnoreCase("pdf")) {
            return new Report("[PDF HEADER: " + title + "]",
                    "[PDF BODY: " + content + "]",
                    "[PDF FOOTER]");
        }
        throw new IllegalArgumentException("prototype report type: " + type);
    }
}
