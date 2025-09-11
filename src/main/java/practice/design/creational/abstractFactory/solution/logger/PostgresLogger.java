package practice.design.creational.abstractFactory.solution.logger;

public class PostgresLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[Postgres log] " + message);
    }
}
