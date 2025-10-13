package practice.design.creational.singleton.solution.module;

import practice.design.creational.singleton.solution.config.AppConfig;

public class DatabaseModule {
    public void connect() {
        AppConfig cfg = AppConfig.getInstance();
        String dsn = cfg.get("db.dsn");
        System.out.println("DB connect: " + dsn);
    }
}
