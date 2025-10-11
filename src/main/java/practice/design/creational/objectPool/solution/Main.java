package practice.design.creational.objectpool.solution;

public class Main {
    public static void main(String[] args) {
        JobRunner runner = new JobRunner();

        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                try {
                    runner.runJob("SELECT * FROM items WHERE id = " + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
