package practice.design.structural.adapter.solution.lib.sms;

public class SmsGateway {
    public boolean send(String phone, String text) {
        System.out.println("[SMS] to=" + phone + " text=" + text);
        return true;
    }
}
