## 📌 Проблема

Уяви, що ти робиш систему для **налаштування підключень до баз даних**.
Є різні бази: `MySQL`, `PostgreSQL`, `MongoDB`.

Зараз розробники напряму створюють конектори, і код розкиданий по всій системі. Це погано, бо:

* важко замінити одну БД на іншу,
* з’являються дублікати коду,
* якщо треба зробити “сімейство” пов’язаних об’єктів (наприклад: **конектор** + **логер** + **конфіг** для MySQL), то доводиться правити всюди.

---

## 📌 Початковий код (без патерну)

```java
// Інтерфейси
interface DbConnector {
    void connect();
}

interface DbLogger {
    void log(String message);
}

// Реалізації MySQL
class MySqlConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MySQL...");
    }
}

class MySqlLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[MySQL log] " + message);
    }
}

// Реалізації PostgreSQL
class PostgresConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to PostgreSQL...");
    }
}

class PostgresLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[Postgres log] " + message);
    }
}

// Реалізації MongoDB
class MongoConnector implements DbConnector {
    public void connect() {
        System.out.println("Connecting to MongoDB...");
    }
}

class MongoLogger implements DbLogger {
    public void log(String message) {
        System.out.println("[Mongo log] " + message);
    }
}

// Використання
public class Main {
    public static void main(String[] args) {
        DbConnector connector = new MySqlConnector();
        DbLogger logger = new MySqlLogger();

        connector.connect();
        logger.log("Some SQL query executed");
    }
}
```

---

## 🎯 Завдання

Перепроєктуй це рішення, використавши **один зі створювальних патернів GoF**, який я обрав.

Вимоги:

* Клієнтський код (`Main`) **не повинен напряму знати про конкретні класи** (`new MySqlConnector()` тощо).
* Можна легко підмінити *сімейство пов’язаних об’єктів* (наприклад, MySQL → PostgreSQL) без зміни логіки.
* Легка розширюваність: додати нову БД = мінімальні зміни у клієнті.
