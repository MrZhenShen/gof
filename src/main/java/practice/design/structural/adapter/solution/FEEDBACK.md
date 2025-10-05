🔥 Гарна робота! Ти фактично застосував **Adapter**: кожен із класів `EmailNotifier`, `SmsNotifier`, `SlackNotifier` адаптує несумісні SDK (Email/SMS/Slack) до єдиного інтерфейсу `Notifier.send(String to, String message)`.
Клієнтський код більше не знає про сторонні типи — це саме те, що треба ✅

Разом з тим, у твоїй композиції є елемент **Strategy** (через `setNotifier(...)` ти динамічно підміняєш стратегію відправки). Це ок: “адаптери” реалізують інтерфейс стратегії.

## Що добре

* Єдиний доменний інтерфейс `Notifier`.
* Вся робота зі сторонніми SDK — всередині адаптерів.
* Немає `if/else` по провайдерах у бізнес-коді.

## Що підкрутити

1. **Вибір провайдера без ручного `setNotifier`**
   Щоб клієнт не знав про конкретні класи, додай **реєстр/фабрику**:

   ```java
   enum Channel { EMAIL, SMS, SLACK }

   final class NotifierRegistry {
       private final Map<Channel, Notifier> map = Map.of(
           Channel.EMAIL, new EmailNotifier(),
           Channel.SMS, new SmsNotifier(),
           Channel.SLACK, new SlackNotifier()
       );
       Notifier get(Channel c) { return map.get(c); }
   }

   // Використання:
   NotifierRegistry reg = new NotifierRegistry();
   NotificationManager nm = new NotificationManager();
   nm.setNotifier(reg.get(Channel.EMAIL)).notify("user@example.com", "Welcome!");
   ```

   Або передавай `Channel` у `notify`, а менеджер сам дістає адаптер.

2. **Виняток**
   `NotifierNotFoundException` не має наслідувати `ClassNotFoundException`. Зроби власний:

   ```java
   public class NotifierNotFoundException extends RuntimeException {
       public NotifierNotFoundException() { super("Notifier is not set"); }
   }
   ```

   (Або застосуй патерн **Null Object** — “порожній” `Notifier`, який логуватиме попередження замість кидати виняток.)

3. **Тестованість / залежності**
   Уникай `static final` для SDK-клієнтів в адаптерах — краще **ін’єкція через конструктор**:

   ```java
   public class EmailNotifier implements Notifier {
       private final EmailClient client;
       public EmailNotifier(EmailClient client) { this.client = client; }
       // ...
   }
   ```

   Так простіше мокати в тестах.

4. **Однорідний семантичний інтерфейс**
   У Slack “отримувач” — канал, а не email/phone. Це нормально для Adapter, але корисно задокументувати це в інтерфейсі `Notifier.send(to, message)` (“to” = destination identifier).

5. **Менеджер без fluent-мутацій** (опційно)
   Поточний fluent-API з `setNotifier(...).notify(...)` ок, але часто чистіше зробити `NotificationService` з мапою адаптерів та методами:

   ```java
   public class NotificationService {
       private final Map<Channel, Notifier> notifiers;
       public void notify(Channel channel, String to, String message) {
           var n = notifiers.get(channel);
           if (n == null) throw new NotifierNotFoundException();
           n.send(to, message);
       }
   }
   ```

### Крихітний рефреш прикладу

```java
public interface Notifier { void send(String to, String message); }

public class EmailNotifier implements Notifier {
    private final EmailClient client;
    public EmailNotifier(EmailClient client) { this.client = client; }
    public void send(String to, String message) {
        client.sendEmail(new EmailMessage(to, "Notification", message));
    }
}

public class NotificationService {
    private final Map<Channel, Notifier> registry;
    public NotificationService(Map<Channel, Notifier> registry) { this.registry = registry; }
    public void notify(Channel c, String to, String msg) {
        Notifier n = registry.get(c);
        if (n == null) throw new NotifierNotFoundException();
        n.send(to, msg);
    }
}
```

## Висновок

* Патерн **Adapter** реалізовано коректно ✅
* Додай реєстр/фабрику, виправ виняток, ін’єкцію залежностей — і матимеш чисту, тестовану та легко розширювану систему.
