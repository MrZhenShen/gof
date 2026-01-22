package practice.design.creational.objectpoolplatforminternals.solution;

import java.util.concurrent.atomic.AtomicInteger;

public final class Worker {
    private static final AtomicInteger SEQ = new AtomicInteger(0);

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
        System.out.println("Created Worker #" + id);
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
        System.out.printf("Worker #%d processed '%s' (count=%d)%n", id, jobName, processed);
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