package practice.design.creational.abstractfactory.solution.connector;

public class MySqlConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MySQL...");
    }
}
