package practice.design.structural.adapter.solution.lib.email;

public class EmailClient {
    public void sendEmail(EmailMessage msg) {
        System.out.println("[Email] to=" + msg.to + " subj=" + msg.subject + " body=" + msg.body);
    }
}
