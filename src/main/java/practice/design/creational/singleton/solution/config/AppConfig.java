package practice.design.creational.singleton.solution.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class AppConfig {
    private static AppConfig config;
    private final Map<String, String> values;

    private AppConfig() {
        try {
            System.out.println("[AppConfig] Loading config from config.properties...");
            values = Files.readAllLines(Path.of("src/main/resources/config.properties"))
                    .stream()
                    .filter(l -> l.contains("="))
                    .map(l -> l.split("=", 2))
                    .collect(java.util.stream.Collectors.toConcurrentMap(a -> a[0].trim(), a -> a[1].trim()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static synchronized AppConfig getInstance() {
        if (config == null) {
            config = new AppConfig();
        }
        return config;
    }

    public String get(String key) {
        return values.get(key);
    }

    // Небезпечне оновлення — кожен інстанс оновлює «свою» копію
    public void reload() {
        System.out.println("[AppConfig] Reloading...");
        config = new AppConfig();
    }
}
