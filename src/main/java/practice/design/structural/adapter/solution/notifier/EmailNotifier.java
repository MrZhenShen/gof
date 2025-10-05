package practice.design.structural.adapter.solution.notifier;

import practice.design.structural.adapter.solution.lib.email.EmailClient;
import practice.design.structural.adapter.solution.lib.email.EmailMessage;

public class EmailNotifier implements Notifier {
    private static final EmailClient emailClient = new EmailClient();

    @Override
    public void send(String consumer, String body) {
        send(consumer, body, "Notification");
    }

    public void send(String consumer, String body, String subject) {
        sendEmailMessage(new EmailMessage(consumer, subject, body));
    }

    private void sendEmailMessage(EmailMessage emailMessage) {
        emailClient.sendEmail(emailMessage);
    }
}
