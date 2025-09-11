package practice.design.creational.abstractFactory.solution;

import practice.design.creational.abstractFactory.solution.factory.DbFactory;
import practice.design.creational.abstractFactory.solution.factory.MySqlFactory;

public class Main {
    public static void main(String[] args) {
        DbFactory dbFactory = new MySqlFactory();
        dbFactory.createConnector().connect();
        dbFactory.createLogger().log("Some SQL query executed");
    }
}
