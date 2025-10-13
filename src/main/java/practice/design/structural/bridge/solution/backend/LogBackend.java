package practice.design.structural.bridge.solution.backend;

import java.util.List;
import practice.design.structural.bridge.solution.model.LogEvent;

public interface LogBackend {
    void write(LogEvent e);

    void writeBatch(List<LogEvent> batch);
}
