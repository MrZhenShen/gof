package practice.design.creational.abstractFactory.solution.factory;

import practice.design.creational.abstractFactory.solution.connector.DbConnector;
import practice.design.creational.abstractFactory.solution.connector.PostgresConnector;
import practice.design.creational.abstractFactory.solution.logger.DbLogger;
import practice.design.creational.abstractFactory.solution.logger.PostgresLogger;

public class PostgresFactory implements DbFactory {

    @Override
    public DbConnector createConnector() {
        return new PostgresConnector();
    }

    @Override
    public DbLogger createLogger() {
        return new PostgresLogger();
    }
}
