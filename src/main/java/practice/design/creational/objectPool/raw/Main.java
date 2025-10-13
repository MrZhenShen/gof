package practice.design.creational.objectpool.raw;

import java.util.Random;

// Умовно "дорогий" ресурс
class ExpensiveConnection {
    private final int id;
    private boolean open;

    ExpensiveConnection(int id) {
        this.id = id;
        simulateHeavyInit(); // довга ініціалізація
        this.open = true;
        System.out.println("[conn#" + id + "] opened");
    }

    private void simulateHeavyInit() {
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
    }

    public void execute(String sql) {
        if (!open) throw new IllegalStateException("Connection is closed");
        System.out.println("[conn#" + id + "] exec: " + sql);
        // Умовне виконання
        try { Thread.sleep(50 + new Random().nextInt(100)); } catch (InterruptedException ignored) {}
    }

    public void close() {
        open = false;
        System.out.println("[conn#" + id + "] closed");
    }
}

// Наївний Runner: кожна задача створює/закриває новий ресурс
class JobRunner {
    private static int counter = 0;

    public void runJob(String sql) {
        ExpensiveConnection conn = new ExpensiveConnection(++counter);
        try {
            conn.execute(sql);
        } finally {
            conn.close();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        JobRunner runner = new JobRunner();

        // Імітація паралельних задач
        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                runner.runJob("SELECT * FROM items WHERE id = " + i);
            }
        };

        Thread t1 = new Thread(task, "t1");
        Thread t2 = new Thread(task, "t2");
        Thread t3 = new Thread(task, "t3");

        long t0 = System.currentTimeMillis();
        t1.start(); t2.start(); t3.start();
        try { t1.join(); t2.join(); t3.join(); } catch (InterruptedException ignored) {}

        long elapsed = System.currentTimeMillis() - t0;
        System.out.println("Elapsed ms: " + elapsed);
    }
}