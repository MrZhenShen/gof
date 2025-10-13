package practice.design.structural.adapter.solution.notifier;

public interface Notifier {
    void send(String consumerIdentity, String content);
}

