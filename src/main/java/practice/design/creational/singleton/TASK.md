## 📌 Проблема

Є застосунок, у якому різні модулі читають **конфігурацію** (наприклад, параметри БД, рівень логування, таймаути).
Зараз кожен модуль створює **свій екземпляр** конфігуратора і щоразу заново завантажує дані з файлу/мережі. В мультипоточному режимі це призводить до:

* дублювання роботи (конфіг грузиться багато разів),
* розсинхронізації (модулі бачать різні стані),
* потенційних гонок при одночасному читанні/оновленні.

Хочемо мати **єдину точку доступу** до конфігурації, з контрольованим життєвим циклом і потокобезпекою.

---

## 📌 Початковий код (без патерну)

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

class AppConfig {
    private Map<String, String> values;

    public AppConfig() {
        // Імітація "важкого" завантаження
        try {
            System.out.println("[AppConfig] Loading config from config.properties...");
            // Умовно: кожен рядок k=v
            values = Files.readAllLines(Path.of("config.properties"))
                    .stream()
                    .filter(l -> l.contains("="))
                    .map(l -> l.split("=", 2))
                    .collect(java.util.stream.Collectors.toMap(a -> a[0].trim(), a -> a[1].trim()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public String get(String key) {
        return values.get(key);
    }

    // Небезпечне оновлення — кожен інстанс оновлює «свою» копію
    public void reload() {
        System.out.println("[AppConfig] Reloading...");
        new AppConfig(); // ❌ створює новий об'єкт, але нікому не віддає і не синхронізує
    }
}

// Модуль А
class MetricsModule {
    void start() {
        AppConfig cfg = new AppConfig(); // ❌ кожен модуль створює свій екземпляр
        String endpoint = cfg.get("metrics.endpoint");
        System.out.println("Metrics to: " + endpoint);
    }
}

// Модуль B
class DatabaseModule {
    void connect() {
        AppConfig cfg = new AppConfig(); // ❌ знову новий
        String dsn = cfg.get("db.dsn");
        System.out.println("DB connect: " + dsn);
    }
}

// Демонстрація гонок
public class Main {
    public static void main(String[] args) {
        // Паралельний старт модулів
        Thread t1 = new Thread(() -> new MetricsModule().start());
        Thread t2 = new Thread(() -> new DatabaseModule().connect());
        t1.start();
        t2.start();

        // Хтось вирішив "перечитати" конфіг
        AppConfig tmp = new AppConfig(); // ❌ ще один екземпляр
        tmp.reload(); // ❌ не синхронізує споживачів
    }
}
```

---

## 🎯 Завдання

Перепроєктуй цю систему, застосувавши **один зі створювальних патернів GoF**, який я обрав.
Вимоги до рішення:

* Є **єдине джерело правди** для конфігурації, доступне всім модулям.
* **Потокобезпечно** (жодних гонок при першому доступі/перезчитуванні).
* Легка інтеграція з кодом модулів (мінімальні зміни в їх API).
* Опційно: підтримай **lazy** ініціалізацію та метод `reload()` так, щоб споживачі почали бачити нові значення узгоджено.
