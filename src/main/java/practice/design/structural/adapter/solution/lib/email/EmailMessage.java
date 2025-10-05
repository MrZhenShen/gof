package practice.design.structural.adapter.solution.lib.email;

public class EmailMessage {
    String to;
    String subject;
    String body;

    public EmailMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
}
