package practice.design.creational.abstractFactory.solution.connector;

public class MongoConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MongoDB...");
    }
}
