package practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.exception.WorkerWaitTimeoutException;
import practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.provider.Worker;

public class WorkerPool {
    private static final WorkerPool INSTANCE = new WorkerPool();
    private static final int POOL_SIZE = 4;
    private static final long ACQUIRE_WAIT_THRESHOLD = 2_000;
    private final BlockingQueue<Worker> available = new ArrayBlockingQueue<>(POOL_SIZE);
    private final BlockingQueue<Worker> inUse = new ArrayBlockingQueue<>(POOL_SIZE);

    private WorkerPool() {
    }

    public static WorkerPool getInstance() {
        return INSTANCE;
    }

    public synchronized Worker acquire() throws InterruptedException {
        long deadline = System.currentTimeMillis() + ACQUIRE_WAIT_THRESHOLD;
        while (available.isEmpty() && (inUse.size() >= POOL_SIZE)) {
            long remaining = deadline - System.currentTimeMillis();
            if (remaining <= 0) {
                throw new WorkerWaitTimeoutException();
            }

            wait(remaining);
        }

        Worker instance;
        if (!available.isEmpty()) {
            instance = available.poll();
        } else {
            instance = new Worker();
        }
        inUse.add(instance);
        return instance;
    }

    public synchronized void release(Worker worker) {
        if (inUse.remove(worker)) {
            worker.reset();
            available.add(worker);
            notifyAll();
        } else {
            throw new IllegalArgumentException("Worker doesn't belong to this pool:" + worker.id());
        }
    }
}
