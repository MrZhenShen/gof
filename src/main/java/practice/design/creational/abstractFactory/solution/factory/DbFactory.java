package practice.design.creational.abstractFactory.solution.factory;

import practice.design.creational.abstractFactory.solution.connector.DbConnector;
import practice.design.creational.abstractFactory.solution.logger.DbLogger;

public interface DbFactory {
    DbConnector createConnector();
    DbLogger createLogger();
}
