🔥 Це **Singleton** — і загалом ти влучив у ціль. Клієнти беруть конфіг через `AppConfig.getInstance()`, а не створюють нові екземпляри — супер ✅

## Що добре

* Лінива ініціалізація через `getInstance()` (один екземпляр на процес).
* Модулі не знають, як конфіг завантажується.
* `reload()` дійсно оновлює глобальне посилання (нові звернення отримають нову версію).

## Де підкрутити

1. **Блокування/продуктивність**
   `synchronized` на всьому `getInstance()` — простий і безпечний варіант, але створює вузьке місце. Краще «Initialization-on-demand holder» (без синхронізації під час читання):

```java
public final class AppConfig {
    private final Map<String,String> values;

    private AppConfig(Map<String,String> values) { this.values = values; }

    private static class Holder {
        static final AtomicReference<AppConfig> REF = new AtomicReference<>(loadFresh());
    }

    public static AppConfig getInstance() {
        return Holder.REF.get(); // без блокувань
    }

    public static void reload() {
        Holder.REF.set(loadFresh()); // атомарне оновлення для всіх потоків
    }

    private static AppConfig loadFresh() {
        try {
            System.out.println("[AppConfig] Loading config...");
            Map<String,String> m =
                Files.readAllLines(Path.of("src/main/resources/config.properties"))
                     .stream()
                     .filter(l -> l.contains("="))
                     .map(l -> l.split("=", 2))
                     .collect(java.util.stream.Collectors.toMap(a -> a[0].trim(), a -> a[1].trim()));
            return new AppConfig(Collections.unmodifiableMap(m)); // іммутабельно
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public String get(String key) { return values.get(key); }
}
```

* Читачі завжди бачать **узгоджений** знімок конфіга.
* Жодних гонок: оновлення — атомарна заміна посилання.

2. **Іммутабельність**
   Зроби `values` **не змінним** (`unmodifiableMap`), а клас — `final`. Це гарантує, що після створення екземпляр конфіга не «пливе».

3. **API перезавантаження**
   Ти викликаєш `tmp.reload()`, але семантично це операція на *класі*, не на екземплярі. Краще `AppConfig.reload()` (статичний), як у прикладі вище.

4. **Шлях до файлу / формат**
   Краще читати з `Properties` або брати шлях з env/system property. Напр., `-Dconfig.file=...`.

5. **Використання в модулях**
   Твій код ок — модулі кожного разу беруть актуальний інстанс:

   ```java
   String dsn = AppConfig.getInstance().get("db.dsn");
   ```

   Якщо хочеш «довгоживучі» посилання — тримай **посилання на значення**, а не на екземпляр:

   ```java
   String endpoint = AppConfig.getInstance().get("metrics.endpoint"); // читання під час старту
   ```

## Підсумок

* Патерн реалізовано вірно ✅
* Для «бойового» варіанту: holder + `AtomicReference` + іммутабельна мапа + статичний `reload()` = безпечне, швидке, чисте рішення.
