package practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.exception.WorkerWaitTimeoutException;
import practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.pool.WorkerPool;
import practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.provider.Worker;

public class JobService {

    private static final WorkerPool pool = WorkerPool.getInstance();
    private static final int MAX_RETRY = 5;
    private static final ConcurrentHashMap<String, Integer> retriedJobsMap = new ConcurrentHashMap<>();
    private final Logger logger = Logger.getLogger(JobService.class.getName());

    public void handle(String jobName) {
        Worker worker = null;

        try {
            worker = pool.acquire();
            worker.process(jobName);
        } catch (InterruptedException e) {
            handleThreadInterruption(jobName);
        } catch (WorkerWaitTimeoutException e) {
            handleWorkerAcquireTimeout(jobName);
        } finally {
            finalzileWorker(worker);
        }
    }

    private void handleThreadInterruption(String jobName) {
        Thread.currentThread().interrupt();
        logger.log(Level.SEVERE, "Job '" + jobName + "' was interrupted.");
    }

    private void handleWorkerAcquireTimeout(String jobName) {
        int attempts = retriedJobsMap.getOrDefault(jobName, 0);
        if (attempts < MAX_RETRY) {
            logger.log(
                    Level.WARNING,
                    String.format(
                            "Retrying job '%s' due to worker acquire timeout (attempt %d)%n",
                            jobName, attempts + 1));
            retriedJobsMap.put(jobName, attempts + 1);
            handle(jobName);
        } else {
            logger.log(Level.WARNING, String.format("Max retry attempts reached for job '%s'. Aborting.%n", jobName));
        }
    }

    private void finalzileWorker(Worker worker) {
        if (worker != null) {
            pool.release(worker);
        }
    }
}