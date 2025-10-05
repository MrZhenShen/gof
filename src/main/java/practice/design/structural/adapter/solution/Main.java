package practice.design.structural.adapter.solution;

import practice.design.structural.adapter.solution.exception.NotifierNotFoundException;
import practice.design.structural.adapter.solution.manager.NotificationManager;
import practice.design.structural.adapter.solution.notifier.EmailNotifier;
import practice.design.structural.adapter.solution.notifier.SlackNotifier;
import practice.design.structural.adapter.solution.notifier.SmsNotifier;

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
