## 📌 Проблема

Є замовлення з життєвим циклом: `NEW → PAID → SHIPPED → DELIVERED`, з можливим `CANCELLED`.
Зараз логіка розкидана по `if/switch`: кожна дія перевіряє поточний статус і вирішує, що робити. Код розростається, важко додавати нові стани та правила (наприклад, `REFUNDED`, `ON_HOLD`).

Хочемо інкапсулювати поведінку **всередині станів**, щоб прибрати розгалуження з доменного класу.

---

## 📌 Початковий код (без патерну)

```java
package demo.state.antipattern;

enum Status { NEW, PAID, SHIPPED, DELIVERED, CANCELLED }

class BusinessRuleViolation extends RuntimeException {
    BusinessRuleViolation(String msg) { super(msg); }
}

class Order {
    private Status status = Status.NEW;

    public Status getStatus() { return status; }

    public void pay() {
        switch (status) {
            case NEW -> status = Status.PAID;
            case PAID, SHIPPED, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Pay not allowed in " + status);
        }
        System.out.println("[pay] -> " + status);
    }

    public void ship() {
        switch (status) {
            case PAID -> status = Status.SHIPPED;
            case NEW, SHIPPED, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Ship not allowed in " + status);
        }
        System.out.println("[ship] -> " + status);
    }

    public void deliver() {
        switch (status) {
            case SHIPPED -> status = Status.DELIVERED;
            case NEW, PAID, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Deliver not allowed in " + status);
        }
        System.out.println("[deliver] -> " + status);
    }

    public void cancel() {
        switch (status) {
            case NEW, PAID -> status = Status.CANCELLED;
            case SHIPPED, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Cancel not allowed in " + status);
        }
        System.out.println("[cancel] -> " + status);
    }
}

public class Main {
    public static void main(String[] args) {
        Order o = new Order();
        o.pay();
        o.ship();
        o.deliver();

        // спроба некоректної операції:
        try { o.cancel(); } catch (BusinessRuleViolation ex) { System.out.println(ex.getMessage()); }
    }
}
```

---

## 🎯 Завдання

Перепроєктуй на **обраний поведінковий патерн**, щоб:

* У класі `Order` **не було** `switch/if` по статусу.
* Кожна операція (`pay()`, `ship()`, `deliver()`, `cancel()`) делегувалася **поточному стану**.
* Додавання нового стану (напр., `ON_HOLD`, `REFUNDED`) не вимагало змін у вже існуючих станах/операціях (мінімально — лише там, де логічно).
* Забезпеч узгоджені переходи між станами (контроль зміни стану централізовано).
* Залиш повідомлення про помилку, якщо операція недопустима в поточному стані.

Можеш залишити API клієнта таким самим:

```java
Order o = new Order();
o.pay();
o.ship();
o.deliver();
```

і додати демонстрацію недопустимих викликів (кидай свій `BusinessRuleViolation`).
