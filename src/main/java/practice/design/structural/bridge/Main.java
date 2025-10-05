package practice.design.structural.bridge;

import practice.design.structural.adapter.solution.lib.slack.SlackClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

record LogEvent(String level, String message, Map<String, String> tags, long ts) {
    LogEvent(String level, String message) {
        this(level, message, Map.of(), System.currentTimeMillis());
    }
}

class Logger {
    LogBackend backend;

    public Logger(LogBackend backend) {
        this.backend = backend;
    }

    public void info(String msg) {
        backend.write(new LogEvent("INFO", msg));
    }

    public void error(String msg) {
        backend.write(new LogEvent("ERROR", msg));
    }
}

class BufferedLogger extends Logger {
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
        if (buf.size() >= batchSize) flush();
    }

    public void flush() {
        for (LogEvent e : buf) backend.write(e);
        buf.clear();
    }
}

class AsyncLogger extends Logger {
    private final BlockingQueue<LogEvent> q = new LinkedBlockingQueue<>();

    AsyncLogger(LogBackend backend) {
        super(backend);

        Thread worker = new Thread(() -> {
            List<LogEvent> batch = new ArrayList<>();
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LogEvent e = q.poll(500, TimeUnit.MILLISECONDS);
                    if (e != null) batch.add(e);
                    if (batch.size() >= 100 || (e == null && !batch.isEmpty())) {
                        backend.write(new ArrayList<>(batch));
                        batch.clear();
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }, "s3-logger");
        worker.start();
    }

    @Override
    public void info(String msg) {
        q.offer(new LogEvent("INFO", msg));
    }

    @Override
    public void error(String msg) {
        q.offer(new LogEvent("ERROR", msg));
    }
}

interface LogBackend {
    void write(LogEvent e);
}

class LogConsole implements LogBackend {
    private static final ConsoleSink sink = new ConsoleSink();

    @Override
    public void write(LogEvent e) {
        sink.write(e);
    }
}

class LogKafka implements LogBackend {
    private static final KafkaSink sink = new KafkaSink();

    @Override
    public void write(LogEvent e) {
        sink.send(e);
    }
}

class LogS3 implements LogBackend {
    private static final S3Sink sink = new S3Sink();

    @Override
    public void write(LogEvent e) {
        sink.putBatch(List.of(e));
    }
}

class ConsoleSink {
    void write(LogEvent e) {
        System.out.println("[CONSOLE] " + e.level() + " " + e.message());
    }
}

class KafkaSink {
    void send(LogEvent e) {
        System.out.println("[KAFKA] " + e.level() + " " + e.message());
    }
}

class S3Sink {
    void putBatch(List<LogEvent> batch) {
        System.out.println("[S3] batch size=" + batch.size());
    }
}


public class Main {

    public static void main(String[] args) {

    }
}
