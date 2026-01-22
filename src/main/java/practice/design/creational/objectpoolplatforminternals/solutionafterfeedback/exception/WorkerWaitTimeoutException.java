package practice.design.creational.objectpoolplatforminternals.solutionafterfeedback.exception;

public class WorkerWaitTimeoutException extends RuntimeException {
    public WorkerWaitTimeoutException(String message) {
        super(message);
    }

    public WorkerWaitTimeoutException() {
        this("Timed out waiting for available Worker from the pool");
    }
}
