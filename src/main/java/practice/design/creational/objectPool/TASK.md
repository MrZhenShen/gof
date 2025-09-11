## 📌 Проблема

У застосунку є задачі, які потребують “важкого” ресурсу — наприклад, мережеве підключення або дорогий рендерер з ініціалізацією 200–500 мс.
Зараз кожна задача створює **новий** екземпляр ресурсу й одразу його закриває. Це повільно, даремно навантажує систему та призводить до піків створення/звільнення об’єктів.

Хочемо:

* **Перевикористовувати** обмежену кількість вже ініціалізованих ресурсів.
* Контролювати **максимальний розмір** одночасно активних ресурсів.
* Підтримати **потокобезпечно** позичання/повернення ресурсу між потоками.
* Опційно: **тайм-аут** очікування ресурсу.

---

## 📌 Початковий код (без патерну)

```java
import java.util.Random;

// Умовно "дорогий" ресурс
class ExpensiveConnection {
    private final int id;
    private boolean open;

    ExpensiveConnection(int id) {
        this.id = id;
        simulateHeavyInit(); // довга ініціалізація
        this.open = true;
        System.out.println("[conn#" + id + "] opened");
    }

    private void simulateHeavyInit() {
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
    }

    public void execute(String sql) {
        if (!open) throw new IllegalStateException("Connection is closed");
        System.out.println("[conn#" + id + "] exec: " + sql);
        // Умовне виконання
        try { Thread.sleep(50 + new Random().nextInt(100)); } catch (InterruptedException ignored) {}
    }

    public void close() {
        open = false;
        System.out.println("[conn#" + id + "] closed");
    }
}

// Наївний Runner: кожна задача створює/закриває новий ресурс
class JobRunner {
    private static int counter = 0;

    public void runJob(String sql) {
        ExpensiveConnection conn = new ExpensiveConnection(++counter);
        try {
            conn.execute(sql);
        } finally {
            conn.close();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        JobRunner runner = new JobRunner();

        // Імітація паралельних задач
        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                runner.runJob("SELECT * FROM items WHERE id = " + i);
            }
        };

        Thread t1 = new Thread(task, "t1");
        Thread t2 = new Thread(task, "t2");
        Thread t3 = new Thread(task, "t3");

        long t0 = System.currentTimeMillis();
        t1.start(); t2.start(); t3.start();
        try { t1.join(); t2.join(); t3.join(); } catch (InterruptedException ignored) {}

        long elapsed = System.currentTimeMillis() - t0;
        System.out.println("Elapsed ms: " + elapsed);
    }
}
```

---

## 🎯 Завдання

Перепроєктуй це рішення, застосувавши **один зі створювальних патернів GoF** (той, що я обрав).
Вимоги до твого рішення:

* Введи **менеджер ресурсу** з методами (назви — на твій смак), наприклад:

    * `borrow()` / `release(resource)`
* Підтримай:

    * **максимальну кількість** активних ресурсів (наприклад, `maxSize = 3`),
    * **потокобезпеку**,
    * **очікування з тайм-аутом** при відсутності вільних ресурсів (опційно).
* `JobRunner` має **перевикористовувати** ресурси через цей менеджер, а не створювати нові напряму.
* Акуратно обробляй помилки: ресурс, який “зламався”, не повертай у пул, тощо (можеш позначати флагом).
