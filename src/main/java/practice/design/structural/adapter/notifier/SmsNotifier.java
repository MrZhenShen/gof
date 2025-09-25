package practice.design.structural.adapter.notifier;

import practice.design.structural.adapter.lib.sms.SmsGateway;

public class SmsNotifier implements Notifier {
    private static final SmsGateway smsGateway = new SmsGateway();

    @Override
    public void send(String consumer, String content) {
        boolean ok = smsGateway.send(consumer, content);
        if (!ok) {
            System.out.println("Failed to send message");
        }
    }
}
