package practice.design.creational.abstractfactory.solution.factory;

import practice.design.creational.abstractfactory.solution.connector.DbConnector;
import practice.design.creational.abstractfactory.solution.connector.MongoConnector;
import practice.design.creational.abstractfactory.solution.logger.DbLogger;
import practice.design.creational.abstractfactory.solution.logger.MongoLogger;

public class MongoFactory implements DbFactory {

    @Override
    public DbConnector createConnector() {
        return new MongoConnector();
    }

    @Override
    public DbLogger createLogger() {
        return new MongoLogger();
    }
}
