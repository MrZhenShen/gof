package practice.design.structural.adapter.lib.sms;

public class SmsGateway {
    public boolean send(String phone, String text) {
        System.out.println("[SMS] to=" + phone + " text=" + text);
        return true;
    }
}
