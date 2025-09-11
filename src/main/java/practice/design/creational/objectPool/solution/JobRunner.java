package practice.design.creational.objectPool.solution;

import java.util.concurrent.atomic.AtomicInteger;

public class JobRunner {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public void runJob(String sql) throws InterruptedException {
        ConnectionPool pool = ConnectionPool.getInstance();

        int id = counter.getAndAdd(1);
        ExpensiveConnection conn = pool.acquire(id);

        try {
            conn.execute(sql);
        } finally {
            pool.release(conn);
        }
    }
}