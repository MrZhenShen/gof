package practice.design.structural.bridge.solution.backend;

import java.util.List;

import practice.design.structural.bridge.solution.model.LogEvent;
import practice.design.structural.bridge.solution.sink.ConsoleSink;

public class ConsoleBackend implements LogBackend {
    private static final ConsoleSink sink = new ConsoleSink();

    @Override
    public void write(LogEvent e) {
        sink.write(e);
    }

    @Override
    public void writeBatch(List<LogEvent> batch) {
        batch.forEach(this::write);
    }
}
