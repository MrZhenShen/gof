package practice.design.creational.abstractfactory.raw;

// Інтерфейси
interface DbConnector {
    void connect();
}

interface DbLogger {
    void log(String message);
}

// Реалізації MySQL
class MySqlConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MySQL...");
    }
}

class MySqlLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[MySQL log] " + message);
    }
}

// Реалізації PostgreSQL
class PostgresConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to PostgreSQL...");
    }
}

class PostgresLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[Postgres log] " + message);
    }
}

// Реалізації MongoDB
class MongoConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MongoDB...");
    }
}

class MongoLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[Mongo log] " + message);
    }
}

// Використання
public class Main {
    public static void main(String[] args) {
        DbConnector connector = new MySqlConnector();
        DbLogger logger = new MySqlLogger();

        connector.connect();
        logger.log("Some SQL query executed");
    }
}
