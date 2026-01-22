package practice.design.creational.objectpoolplatforminternals.solution;

public class JobService {

    private static final WorkerPool pool = WorkerPool.getInstance();

    public void handle(String jobName) {
        Worker w = null;

        try {
            w = pool.acquire();
            w.process(jobName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.release(w);
        }
    }
}