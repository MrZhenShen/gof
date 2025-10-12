## 📌 Проблема

Є команда розробників, які працюють у спільному чаті.
Коли один учасник надсилає повідомлення, усі інші мають його отримати.
Але:

* не хочемо, щоб учасники напряму знали одне про одного;
* хочемо мати центральний компонент, який координує повідомлення між ними;
* хочемо додавати логіку на рівні “чату” (фільтрація, логування, системні повідомлення тощо) без зміни користувачів.

---

## 📌 Початковий код (без патерну)

```java
package demo.chat.antipattern;

import java.util.ArrayList;
import java.util.List;

class User {
    private final String name;
    private final List<User> others = new ArrayList<>();

    public User(String name) { this.name = name; }

    public void connect(User other) { others.add(other); }

    public void send(String message) {
        System.out.println(name + " sends: " + message);
        for (User u : others) {
            u.receive(message, name);
        }
    }

    public void receive(String message, String from) {
        System.out.println("  " + name + " got from " + from + ": " + message);
    }
}

public class Main {
    public static void main(String[] args) {
        User alice = new User("Alice");
        User bob   = new User("Bob");
        User carol = new User("Carol");

        // Прямі посилання
        alice.connect(bob);
        alice.connect(carol);
        bob.connect(alice);
        carol.connect(alice);

        alice.send("Hello team!");
        bob.send("Hi Alice!");
    }
}
```

---

## ❌ Проблеми цього підходу

* Кожен користувач мусить **знати всіх інших**.
* Логіка розсилки, фільтрації, правил тощо — **всередині кожного користувача**.
* Якщо треба додати функцію “mute” або “system message”, доведеться правити всіх.

---

## 🎯 Завдання

Перепроєктуй це рішення, застосувавши **один із поведінкових патернів GoF**, який я обрав.

Вимоги:

1. Користувач не має напряму знати інших користувачів.
2. Має існувати центральний компонент, який керує взаємодією (реєстрацією, розсилкою).
3. Підтримай такі дії:

   * `send(String msg)` — повідомлення всім, окрім відправника;
   * `sendTo(String msg, String recipient)` — приватне повідомлення;
   * `broadcastSystem(String msg)` — системне сповіщення для всіх користувачів;
4. Покажи в `Main`, як кілька користувачів реєструються, спілкуються й отримують повідомлення, не маючи один одного в посиланнях.

---

Хочеш бонус? Додай:

* логування всіх повідомлень у чаті (через той самий центральний об’єкт);
* “mute” — щоб користувач тимчасово не отримував повідомлень від певного іншого користувача.
