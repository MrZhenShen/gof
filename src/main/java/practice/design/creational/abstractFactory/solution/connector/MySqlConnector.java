package practice.design.creational.abstractFactory.solution.connector;

public class MySqlConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MySQL...");
    }
}
