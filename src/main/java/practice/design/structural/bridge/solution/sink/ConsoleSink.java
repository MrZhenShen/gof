package practice.design.structural.bridge.solution.sink;

import practice.design.structural.bridge.solution.model.LogEvent;

public class ConsoleSink {
    public void write(LogEvent e) {
        System.out.println("[CONSOLE] " + e.level() + " " + e.message());
    }
}
