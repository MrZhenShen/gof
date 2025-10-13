package practice.design.structural.bridge.solution.sink;

import practice.design.structural.bridge.solution.model.LogEvent;

public class KafkaSink {
    public void send(LogEvent e) {
        System.out.println("[KAFKA] " + e.level() + " " + e.message());
    }
}
