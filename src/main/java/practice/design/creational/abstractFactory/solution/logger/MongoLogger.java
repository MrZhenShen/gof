package practice.design.creational.abstractFactory.solution.logger;

public class MongoLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[Mongo log] " + message);
    }
}
