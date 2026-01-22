package practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.provider;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Worker {
    private static final AtomicInteger SEQ = new AtomicInteger(0);
    private final Logger logger = Logger.getLogger(Worker.class.getName());

    private final int id;
    private String lastJobName;
    private int processed;

    public Worker() {
        this.id = SEQ.incrementAndGet();
        // Simulate expensive creation
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.log(Level.INFO, "Created Worker #" + id);
    }

    public void process(String jobName) {
        // Simulate some stateful work
        this.lastJobName = jobName;
        this.processed++;
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // logger.log(Level.INFO, String.format("Worker #%d processed '%s' (count=%d)%n", id, jobName, processed));
    }

    public void reset() {
        // Required between jobs to avoid state leaks
        lastJobName = null;
    }

    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return "Worker{id=" + id + ", processed=" + processed + ", lastJobName=" + lastJobName + "}";
    }
}