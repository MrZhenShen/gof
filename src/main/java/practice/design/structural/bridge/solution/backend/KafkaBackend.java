package practice.design.structural.bridge.solution.backend;

import java.util.List;

import practice.design.structural.bridge.solution.model.LogEvent;
import practice.design.structural.bridge.solution.sink.KafkaSink;

public class KafkaBackend implements LogBackend {
    private static final KafkaSink sink = new KafkaSink();

    @Override
    public void write(LogEvent e) {
        sink.send(e);
    }

    @Override
    public void writeBatch(List<LogEvent> batch) {
        batch.forEach(this::write);
    }
}
