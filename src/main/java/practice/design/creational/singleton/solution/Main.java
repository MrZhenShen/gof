package practice.design.creational.singleton.solution;

import practice.design.creational.singleton.solution.config.AppConfig;
import practice.design.creational.singleton.solution.module.DatabaseModule;
import practice.design.creational.singleton.solution.module.MetricsModule;

public class Main {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> new MetricsModule().start());
        Thread t2 = new Thread(() -> new DatabaseModule().connect());
        t1.start();
        t2.start();

        AppConfig tmp = AppConfig.getInstance();
        tmp.reload();
    }
}