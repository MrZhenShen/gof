package practice.design.structural.bridge.solution.sink;

import java.util.List;
import practice.design.structural.bridge.solution.model.LogEvent;

public class S3Sink {
    public void putBatch(List<LogEvent> batch) {
        System.out.println("[S3] batch size=" + batch.size());
    }
}
