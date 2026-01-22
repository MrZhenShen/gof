package practice.design.creational.objectpoolplatforminternals.raw;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int threads = 12;
        int jobs = 40;

        ExecutorService exec = Executors.newFixedThreadPool(threads);
        JobService service = new JobService();

        List<Future<?>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 1; i <= jobs; i++) {
            String name = "job-" + i;
            futures.add(exec.submit(() -> service.handle(name)));
        }

        for (Future<?> f : futures) f.get();
        exec.shutdown();

        long took = System.currentTimeMillis() - start;
        System.out.println("\nDONE in " + took + " ms");
    }
}