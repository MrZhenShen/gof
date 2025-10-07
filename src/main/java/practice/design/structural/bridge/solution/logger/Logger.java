package practice.design.structural.bridge.solution.logger;

import practice.design.structural.bridge.solution.backend.LogBackend;
import practice.design.structural.bridge.solution.model.LogEvent;

public class Logger {
    LogBackend backend;

    public Logger(LogBackend backend) {
        this.backend = backend;
    }

    public void info(String msg) {
        backend.write(new LogEvent("INFO", msg));
    }

    public void error(String msg) {
        backend.write(new LogEvent("ERROR", msg));
    }
}