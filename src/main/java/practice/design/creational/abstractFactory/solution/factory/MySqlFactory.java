package practice.design.creational.abstractfactory.solution.factory;

import practice.design.creational.abstractfactory.solution.connector.DbConnector;
import practice.design.creational.abstractfactory.solution.connector.MySqlConnector;
import practice.design.creational.abstractfactory.solution.logger.DbLogger;
import practice.design.creational.abstractfactory.solution.logger.MySqlLogger;

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

