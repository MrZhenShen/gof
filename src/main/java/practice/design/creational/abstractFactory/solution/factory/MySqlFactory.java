package practice.design.creational.abstractFactory.solution.factory;

import practice.design.creational.abstractFactory.solution.connector.DbConnector;
import practice.design.creational.abstractFactory.solution.connector.MySqlConnector;
import practice.design.creational.abstractFactory.solution.logger.DbLogger;
import practice.design.creational.abstractFactory.solution.logger.MySqlLogger;

public class MySqlFactory implements DbFactory {

    @Override
    public DbConnector createConnector() {
        return new MySqlConnector();
    }

    @Override
    public DbLogger createLogger() {
        return new MySqlLogger();
    }
}

