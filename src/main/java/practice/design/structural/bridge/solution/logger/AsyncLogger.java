package practice.design.structural.bridge.solution.logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import practice.design.structural.bridge.solution.backend.LogBackend;
import practice.design.structural.bridge.solution.model.LogEvent;

public class AsyncLogger extends Logger {
    private final BlockingQueue<LogEvent> q = new LinkedBlockingQueue<>();

    public AsyncLogger(LogBackend backend) {
        this(backend, 100, Duration.ofMillis(500));
    }

    public AsyncLogger(LogBackend backend, int maxBatchSize, Duration duration) {
        super(backend);

        Thread worker = new Thread(() -> {
            List<LogEvent> batch = new ArrayList<>();
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LogEvent e = q.poll(duration.toMillis(), TimeUnit.MILLISECONDS);
                    if (e != null)
                        batch.add(e);
                    if (batch.size() >= maxBatchSize || (e == null && !batch.isEmpty())) {
                        backend.writeBatch(new ArrayList<>(batch));
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
