## 📌 Проблема

У застосунку є сервіс сповіщень. Потрібно вміти відправляти повідомлення різними провайдерами: **Email**, **SMS**, **Slack** (3rd-party SDK).
Зараз клієнтський код напряму знає про всі SDK та їхні несумісні інтерфейси — купа `if/else`, важко підміняти провайдери, тестувати і розширювати.

Хочемо:

* Єдиний **уніфікований інтерфейс** відправки сповіщень у коді застосунку.
* Мінімум змін при додаванні нового провайдера.
* Винести залежність від сторонніх SDK з доменного коду.

---

## 📌 Початковий код (без патерну)

```java
// Умовні сторонні SDK з різними інтерфейсами

// Email SDK
class EmailMessage {
    String to;
    String subject;
    String body;
    EmailMessage(String to, String subject, String body) {
        this.to = to; this.subject = subject; this.body = body;
    }
}
class EmailClient {
    public void sendEmail(EmailMessage msg) {
        System.out.println("[Email] to=" + msg.to + " subj=" + msg.subject + " body=" + msg.body);
    }
}

// SMS SDK
class SmsGateway {
    public boolean send(String phone, String text) {
        System.out.println("[SMS] to=" + phone + " text=" + text);
        return true;
    }
}

// Slack SDK
class SlackClient {
    public void postMessage(String channelId, String text) {
        System.out.println("[Slack] #" + channelId + " -> " + text);
    }
}

// ===== Код застосунку (залежить від усіх SDK) =====

class NotificationManager {

    // ❌ Несумісні інтерфейси → багато розгалужень і знань про сторонні типи
    public void notify(String provider, String recipient, String message) {
        if ("email".equalsIgnoreCase(provider)) {
            EmailClient client = new EmailClient();
            EmailMessage msg = new EmailMessage(recipient, "Notification", message);
            client.sendEmail(msg);

        } else if ("sms".equalsIgnoreCase(provider)) {
            SmsGateway gw = new SmsGateway();
            boolean ok = gw.send(recipient, message);
            if (!ok) {
                System.out.println("SMS failed");
            }

        } else if ("slack".equalsIgnoreCase(provider)) {
            SlackClient slack = new SlackClient();
            // ⚠️ У Slack отримувач — це channelId, а не email/phone
            slack.postMessage(recipient, "[NOTIFY] " + message);

        } else {
            throw new IllegalArgumentException("Unknown provider: " + provider);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        NotificationManager nm = new NotificationManager();
        nm.notify("email", "user@example.com", "Welcome!");
        nm.notify("sms", "+380501112233", "Your code: 1234");
        nm.notify("slack", "alerts", "CPU > 90%");
    }
}
```

---

## 🎯 Завдання

Перепроєктуй цей код, застосувавши **один зі структурних патернів GoF** (той, що я випадково обрав).
Вимоги:

* У коді застосунку має бути **єдиний інтерфейс** сповіщення на кшталт `Notifier.send(String to, String message)`.
* Жодних `if/else` з перевірками типу провайдера в клієнтському коді.
* Легка підміна/додавання провайдера (Email/SMS/Slack/інший) без змін у бізнес-логіці.
* Сторонні SDK мають лишитися “за межами” доменного коду.
