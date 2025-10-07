package practice.design.structural.bridge.solution.backend;

import java.util.List;
import practice.design.structural.bridge.solution.model.LogEvent;
import practice.design.structural.bridge.solution.sink.S3Sink;

public class S3Backend implements LogBackend {
    private static final S3Sink sink = new S3Sink();

    @Override
    public void write(LogEvent e) {
        sink.putBatch(List.of(e));
    }

    @Override
    public void writeBatch(List<LogEvent> batch) {
        sink.putBatch(batch);
    }
}
