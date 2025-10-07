package practice.design.structural.bridge.solution.logger;

import java.util.ArrayList;
import java.util.List;
import practice.design.structural.bridge.solution.backend.LogBackend;
import practice.design.structural.bridge.solution.model.LogEvent;

public class BufferedLogger extends Logger {
    private final List<LogEvent> buf = new ArrayList<>();
    private final int batchSize;

    public BufferedLogger(LogBackend backend, int batchSize) {
        super(backend);
        this.batchSize = batchSize;
    }

    @Override
    public void info(String msg) {
        append(new LogEvent("INFO", msg));
    }

    @Override
    public void error(String msg) {
        append(new LogEvent("ERROR", msg));
    }

    private void append(LogEvent e) {
        buf.add(e);
        if (buf.size() >= batchSize)
            flush();
    }

    public void flush() {
        for (LogEvent e : buf)
            backend.write(e);
        buf.clear();
    }
}
