package practice.design.creational.abstractFactory.solution.factory;

import practice.design.creational.abstractFactory.solution.connector.DbConnector;
import practice.design.creational.abstractFactory.solution.connector.MongoConnector;
import practice.design.creational.abstractFactory.solution.logger.DbLogger;
import practice.design.creational.abstractFactory.solution.logger.MongoLogger;

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
