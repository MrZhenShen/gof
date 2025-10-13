Клас! Це саме **Object Pool** — і загальна ідея реалізована правильно ✅
Є пул, `borrow`/`release` (у тебе `acquire`/`release`), ліміт `MAX_SIZE`, синхронізація та повторне використання ресурсів.

Нижче — що підправити, щоб рішення було надійним у проді.

## Що добре

* Перевикористання “важких” конекшенів замість створення кожного разу.
* Центральний менеджер (`ConnectionPool`) з контрольованим максимумом.
* Потокобезпечні `synchronized` методи та `wait/notifyAll`.

## Зауваження та покращення

1. **Баг із `ArrayList.removeLast()`**
   `ArrayList` не має `removeLast()` (окрім дуже нових версій JDK). Використай `Deque`:

   ```java
   private final Deque<ExpensiveConnection> available = new ArrayDeque<>();
   // ...
   ExpensiveConnection instance = available.removeLast(); // тепер ок
   ```

2. **`wait()` без `while` ⇒ ризик спуріозних пробуджень**
   Завжди перевіряй умову в циклі:

   ```java
   while (available.isEmpty() && inUse.size() >= MAX_SIZE) {
       wait();
   }
   ```

3. **Ідентифікатор з’єднання створює Job, а не пул**
   Ідентифікатор — властивість ресурсу, тож хай **пул** призначає його при створенні. Не передавай `id` у `acquire()`.
   Зараз різні задачі можуть випадково передати однаковий id.

4. **Ресурс може бути “закритим”**
   Якщо хтось викличе `close()`, пул потім поверне **закритий** екземпляр. Перевір на вході й відбраковуй:

   ```java
   if (!instance.isOpen()) instance = new ExpensiveConnection(nextId());
   ```

5. **Тайм-аут очікування** (опційно, але корисно)
   Додай перевантаження `acquire(long timeoutMs)` і кинь `TimeoutException`, якщо вільного ресурсу нема.

6. **Singleton пулу — зроби thread-safe**
   `getInstance()` без синхронізації може створити 2 екземпляри при гонці. Краще Holder:

   ```java
   public static ConnectionPool getInstance() { return Holder.INSTANCE; }
   private static class Holder { static final ConnectionPool INSTANCE = new ConnectionPool(); }
   ```

7. **Скидання стану перед поверненням**
   Якщо ресурс має внутрішній стан, варто його нормалізувати у `release` (у тебе поки що нема, але good practice).

---

## Оновлений варіант (стисло)

```java
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public final class ConnectionPool {
    private static final int MAX_SIZE = 3;

    private final Deque<ExpensiveConnection> available = new ArrayDeque<>();
    private int inUseCount = 0;
    private final AtomicInteger ids = new AtomicInteger(0);

    private ConnectionPool() {}

    public static ConnectionPool getInstance() { return Holder.INSTANCE; }
    private static class Holder { static final ConnectionPool INSTANCE = new ConnectionPool(); }

    public ExpensiveConnection acquire() throws InterruptedException {
        synchronized (this) {
            while (available.isEmpty() && inUseCount >= MAX_SIZE) {
                wait();
            }
            ExpensiveConnection c = !available.isEmpty() ? available.removeLast()
                                                         : new ExpensiveConnection(ids.incrementAndGet());
            if (!c.isOpen()) {
                c = new ExpensiveConnection(ids.incrementAndGet());
            }
            inUseCount++;
            return c;
        }
    }

    public ExpensiveConnection acquire(long timeoutMs)
            throws InterruptedException, TimeoutException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        synchronized (this) {
            while (available.isEmpty() && inUseCount >= MAX_SIZE) {
                long waitTime = deadline - System.currentTimeMillis();
                if (waitTime <= 0) throw new TimeoutException("Pool acquire timed out");
                wait(waitTime);
            }
            ExpensiveConnection c = !available.isEmpty() ? available.removeLast()
                                                         : new ExpensiveConnection(ids.incrementAndGet());
            if (!c.isOpen()) {
                c = new ExpensiveConnection(ids.incrementAndGet());
            }
            inUseCount++;
            return c;
        }
    }

    public void release(ExpensiveConnection connection) {
        synchronized (this) {
            // якщо зламався — не повертаємо в пул
            if (connection == null || !connection.isOpen()) {
                inUseCount--;
                notifyAll();
                return;
            }
            available.addLast(connection);
            inUseCount--;
            notifyAll();
        }
    }
}
```

```java
import java.util.Random;

public class ExpensiveConnection {
    private final int id;
    private volatile boolean open;

    ExpensiveConnection(int id) {
        this.id = id;
        simulateHeavyInit();
        this.open = true;
        System.out.println("[conn#" + id + "] opened");
    }

    private void simulateHeavyInit() {
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
    }

    public void execute(String sql) {
        if (!open) throw new IllegalStateException("Connection is closed");
        System.out.println("[conn#" + id + "] exec: " + sql);
        try { Thread.sleep(50 + new Random().nextInt(100)); } catch (InterruptedException ignored) {}
    }

    public boolean isOpen() { return open; }
    public void close() { open = false; System.out.println("[conn#" + id + "] closed"); }
}
```

```java
import java.util.concurrent.TimeoutException;

public class JobRunner {
    public void runJob(String sql) throws InterruptedException, TimeoutException {
        ConnectionPool pool = ConnectionPool.getInstance();
        ExpensiveConnection conn = pool.acquire(1_000); // тайм-аут 1s
        try {
            conn.execute(sql);
            // якщо сталася помилка — можна позначити conn.close() і воно не повернеться в пул
        } finally {
            pool.release(conn);
        }
    }
}
```

---

## Висновок

* Патерн **Object Pool** ти втілив правильно ✅
* Основні допили: `Deque`, цикл `while` для `wait`, thread-safe Singleton, і (за бажанням) тайм-аут + перевірка здоров’я ресурсу.
* З таким пулом ти отримаєш стабільний та швидкий перформанс без піків створення/закриття.
