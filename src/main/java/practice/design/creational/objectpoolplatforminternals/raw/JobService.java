package practice.design.creational.objectpoolplatforminternals.raw;

public class JobService {

    public void handle(String jobName) {
        Worker w = new Worker();
        try {
            w.process(jobName);
        } finally {
            w.reset();
        }
    }
}