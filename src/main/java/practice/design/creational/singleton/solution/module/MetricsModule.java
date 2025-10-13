package practice.design.creational.singleton.solution.module;

import practice.design.creational.singleton.solution.config.AppConfig;

public class MetricsModule {
    public void start() {
        AppConfig cfg = AppConfig.getInstance();
        String endpoint = cfg.get("metrics.endpoint");
        System.out.println("Metrics to: " + endpoint);
    }
}
