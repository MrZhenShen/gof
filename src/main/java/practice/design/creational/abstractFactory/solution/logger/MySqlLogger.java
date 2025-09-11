package practice.design.creational.abstractFactory.solution.logger;

public class MySqlLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[MySQL log] " + message);
    }
}
