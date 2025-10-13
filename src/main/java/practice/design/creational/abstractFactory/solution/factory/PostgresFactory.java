package practice.design.creational.abstractfactory.solution.factory;

import practice.design.creational.abstractfactory.solution.connector.DbConnector;
import practice.design.creational.abstractfactory.solution.connector.PostgresConnector;
import practice.design.creational.abstractfactory.solution.logger.DbLogger;
import practice.design.creational.abstractfactory.solution.logger.PostgresLogger;

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
