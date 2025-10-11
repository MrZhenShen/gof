package practice.design.creational.abstractfactory.solution.logger;

public class PostgresLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[Postgres log] " + message);
    }
}
