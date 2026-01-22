package practice.design.creational.objectpoolplatforminternals.solution;

import java.util.ArrayList;
import java.util.List;

public class WorkerPool {
    private static WorkerPool pool;
    private static final int POOL_SIZE = 8;
    private final List<Worker> available = new ArrayList<>();
    private final List<Worker> inUse = new ArrayList<>();

    private WorkerPool() {
    }

    public static WorkerPool getInstance() {
        if (pool == null) {
            pool = new WorkerPool();
        }
        return pool;
    }

    public synchronized Worker acquire() throws InterruptedException {
        while (available.isEmpty() && (inUse.size() >= POOL_SIZE)) {
            wait();
        }

        Worker instance;
        System.out.println("\n--- WorkerPool State ---");
        System.out.println("Available: " + available.size());
        System.out.println("In Use: " + inUse.size());
        if (!available.isEmpty()) {
            instance = available.remove(available.size() - 1);
        } else {
            instance = new Worker();
        }
        inUse.add(instance);
        return instance;
    }

    public synchronized void release(Worker worker) {
        if (inUse.remove(worker)) {
            available.add(worker);
            worker.reset();
            notifyAll();
        } else {
            throw new IllegalArgumentException("Nice try, but that object doesn't belong to this pool.");
        }
    }
}
