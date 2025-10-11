package practice.design.creational.abstractfactory.solution.connector;

public class MongoConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MongoDB...");
    }
}
