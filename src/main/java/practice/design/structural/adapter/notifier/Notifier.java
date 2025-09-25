package practice.design.structural.adapter.notifier;

public interface Notifier {
    void send(String consumerIdentity, String content);
}

