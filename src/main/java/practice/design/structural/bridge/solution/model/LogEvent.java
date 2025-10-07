package practice.design.structural.bridge.solution.model;

import java.util.Map;

public record LogEvent(String level, String message, Map<String, String> tags, long ts) {
    public LogEvent(String level, String message) {
        this(level, message, Map.of(), System.currentTimeMillis());
    }
}
