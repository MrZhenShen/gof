package practice.design.creational.abstractfactory.solution.connector;

public class PostgresConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to PostgreSQL...");
    }
}
