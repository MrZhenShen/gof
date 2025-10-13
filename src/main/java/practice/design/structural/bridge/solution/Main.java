package practice.design.structural.bridge.solution;

import java.time.Duration;
import practice.design.structural.bridge.solution.backend.ConsoleBackend;
import practice.design.structural.bridge.solution.backend.KafkaBackend;
import practice.design.structural.bridge.solution.backend.S3Backend;
import practice.design.structural.bridge.solution.logger.AsyncLogger;
import practice.design.structural.bridge.solution.logger.BufferedLogger;
import practice.design.structural.bridge.solution.logger.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = new Logger(new ConsoleBackend());
        logger.info("hello");

        Logger bufferedLogger = new BufferedLogger(new KafkaBackend(), 3);
        bufferedLogger.info("k1");
        bufferedLogger.info("k2");
        bufferedLogger.error("k3");

        Logger asyncLogger = new AsyncLogger(new S3Backend(), 100, Duration.ofMillis(500));
        asyncLogger.info("s3-1");
        asyncLogger.error("s3-2");
    }
}
