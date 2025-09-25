package practice.design.structural.adapter;

import practice.design.structural.adapter.exception.NotifierNotFoundException;
import practice.design.structural.adapter.manager.NotificationManager;
import practice.design.structural.adapter.notifier.EmailNotifier;
import practice.design.structural.adapter.notifier.SlackNotifier;
import practice.design.structural.adapter.notifier.SmsNotifier;

public class Main {
    public static void main(String[] args) {
        try {
            bulkNotify();
        } catch (NotifierNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void bulkNotify() throws NotifierNotFoundException {
        new NotificationManager()
                .setNotifier(new EmailNotifier())
                .notify("user@example.com", "Welcome!")
                .setNotifier(new SmsNotifier())
                .notify("+380501112233", "Your code: 1234")
                .setNotifier(new SlackNotifier())
                .notify("alerts", "CPU > 90%");
    }
}
