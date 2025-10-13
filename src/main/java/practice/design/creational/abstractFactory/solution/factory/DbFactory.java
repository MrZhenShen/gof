package practice.design.creational.abstractfactory.solution.factory;

import practice.design.creational.abstractfactory.solution.connector.DbConnector;
import practice.design.creational.abstractfactory.solution.logger.DbLogger;

public interface DbFactory {
    DbConnector createConnector();
    DbLogger createLogger();
}
