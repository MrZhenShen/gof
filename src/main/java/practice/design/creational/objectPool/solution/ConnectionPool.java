package practice.design.creational.objectPool.solution;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static ConnectionPool pool;
    private static final int MAX_SIZE = 3;
    private final List<ExpensiveConnection> available = new ArrayList<>();
    private final List<ExpensiveConnection> inUse = new ArrayList<>();

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public synchronized ExpensiveConnection acquire(int id) throws InterruptedException {
        if(available.isEmpty() && (inUse.size() >= MAX_SIZE)) {
            wait();
        }

        ExpensiveConnection instance;
        if (!available.isEmpty()) {
            instance = available.removeLast();
        } else {
            instance = new ExpensiveConnection(id);
        }
        inUse.add(instance);
        return instance;
    }

    public synchronized void release(ExpensiveConnection connection) {
        if (inUse.remove(connection)) {
            available.add(connection);
            notifyAll();
        } else {
            throw new IllegalArgumentException("Nice try, but that object doesn't belong to this pool.");
        }
    }
}
