## 📌 Проблема (домен: логування в проді)

Є фреймворк логування, що вміє писати події в різні “бекенди”:

* **Console**
* **Kafka** (як черга подій)
* **S3/об’єктне сховище** (батчами)

Також є різні **поведінки логера**:

* **Звичайний** (синхронний запис)
* **Буферизований** (накопичує N повідомлень, потім флашить разом)
* **Асинхронний** (кладе в чергу, воркер відправляє у бекенд)

Зараз код жорстко зв’язаний: `ConsoleLogger`, `KafkaBufferedLogger`, `S3AsyncLogger`… на кожну комбінацію свій клас. Розширювати важко: додати новий бекенд × новий режим = вибух кількості класів та дублювання.

**Хочемо** вільно комбінувати “який Logger” з “куди пишемо” без нових класів на кожну комбінацію: наприклад, `new AsyncLogger(new KafkaBackend(...))` або `new BufferedLogger(new S3Backend(...), 500)`.

---

## 📌 Початковий код (без патерну; навмисно “погано”)

```java
import java.util.*;
import java.util.concurrent.*;

// Спільна модель
record LogEvent(String level, String message, Map<String, String> tags, long ts) {
    LogEvent(String level, String message) {
        this(level, message, Map.of(), System.currentTimeMillis());
    }
}

// --- Бекенди (але зараз вони зашиті всередині логерів) ---
class ConsoleSink {
    void write(LogEvent e) {
        System.out.println("[CONSOLE] " + e.level() + " " + e.message());
    }
}

class KafkaSink {
    void send(LogEvent e) {
        // уявна відправка у Kafka
        System.out.println("[KAFKA] " + e.level() + " " + e.message());
    }
}

class S3Sink {
    void putBatch(List<LogEvent> batch) {
        System.out.println("[S3] batch size=" + batch.size());
    }
}

// --- Логери “на кожну комбінацію” (антипатерн) ---
class ConsoleLogger {
    private final ConsoleSink sink = new ConsoleSink();
    public void info(String msg) { sink.write(new LogEvent("INFO", msg)); }
    public void error(String msg) { sink.write(new LogEvent("ERROR", msg)); }
}

class KafkaBufferedLogger {
    private final KafkaSink sink = new KafkaSink();
    private final List<LogEvent> buf = new ArrayList<>();
    private final int batchSize;

    KafkaBufferedLogger(int batchSize) {
        this.batchSize = batchSize;
    }

    public void info(String msg) { append(new LogEvent("INFO", msg)); }
    public void error(String msg) { append(new LogEvent("ERROR", msg)); }

    private void append(LogEvent e) {
        buf.add(e);
        if (buf.size() >= batchSize) flush();
    }

    public void flush() {
        for (LogEvent e : buf) sink.send(e);
        buf.clear();
    }
}

class S3AsyncLogger {
    private final S3Sink sink = new S3Sink();
    private final BlockingQueue<LogEvent> q = new LinkedBlockingQueue<>();
    private final Thread worker;

    S3AsyncLogger() {
        worker = new Thread(() -> {
            List<LogEvent> batch = new ArrayList<>();
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LogEvent e = q.poll(500, TimeUnit.MILLISECONDS);
                    if (e != null) batch.add(e);
                    if (batch.size() >= 100 || (e == null && !batch.isEmpty())) {
                        sink.putBatch(new ArrayList<>(batch));
                        batch.clear();
                    }
                }
            } catch (InterruptedException ignored) {}
        }, "s3-logger");
        worker.start();
    }

    public void info(String msg) { q.offer(new LogEvent("INFO", msg)); }
    public void error(String msg) { q.offer(new LogEvent("ERROR", msg)); }
}

// --- Демонстрація “вибуху” ---
public class Main {
    public static void main(String[] args) {
        ConsoleLogger a = new ConsoleLogger();
        a.info("hello");
        KafkaBufferedLogger b = new KafkaBufferedLogger(3);
        b.info("k1"); b.info("k2"); b.error("k3"); // флаш
        S3AsyncLogger c = new S3AsyncLogger();
        c.info("s3-1"); c.error("s3-2");
    }
}
```

---

## 🎯 Завдання

Перепроєктуй це рішення, використовуючи **один зі структурних патернів GoF** (який я обрав).
Вимоги:

1. Введи **абстракцію логера** (API на кшталт `log(level, msg)` + шорткати `info/error`) і **інтерфейс бекенду** з базовими можливостями запису (одиночний запис та/або батч).
2. Зроби **адаптери бекендів** для `ConsoleSink`, `KafkaSink`, `S3Sink`, що зводять їхні відмінні інтерфейси до єдиного `LogBackend`.
3. Реалізуй **різні логери** (мінімум два), які працюють через **той самий бекенд-інтерфейс**:

  * `BasicLogger` (синхронний, одразу пише в бекенд),
  * `BufferedLogger` (накопичує і флашить батчами або за таймаутом),
  * (опційно) `AsyncLogger` (черга + воркер).
    Важливо: логери не знають конкретного бекенду; бекенд не знає про тип логера.
4. Покажи у `Main`, що можна довільно комбінувати:

  * `new BasicLogger(new ConsoleBackend())`
  * `new BufferedLogger(new KafkaBackend(), 200)`
  * `new AsyncLogger(new S3Backend(), 100, Duration.ofMillis(500))`
    без створення нових класів на кожну комбінацію.
5. (Опційно) Налаштування форматування (`Layout`) зроби окремою абстракцією, яку logger використовує перед передачею у бекенд.

> Ключова ідея — **розвести ієрархії** “логерів” і “бекендів” так, щоб додавання нового бекенду не вимагало нових логерів і навпаки (скейл за формулою **m + n**, а не **m × n**).
