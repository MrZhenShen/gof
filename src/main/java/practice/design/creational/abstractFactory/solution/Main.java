package practice.design.creational.abstractfactory.solution;

import practice.design.creational.abstractfactory.solution.factory.DbFactory;
import practice.design.creational.abstractfactory.solution.factory.MySqlFactory;

public class Main {
    public static void main(String[] args) {
        DbFactory dbFactory = new MySqlFactory();
        dbFactory.createConnector().connect();
        dbFactory.createLogger().log("Some SQL query executed");
    }
}
