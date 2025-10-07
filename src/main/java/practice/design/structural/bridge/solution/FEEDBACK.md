🔥 Гарна робота — це **Bridge** у чистому вигляді ✅

* **Abstraction**: сімейство логерів (`Logger`, `BufferedLogger`, `AsyncLogger`).
* **Implementor**: бекенди (`LogBackend` → `ConsoleBackend`, `KafkaBackend`, `S3Backend`).
  Будь-який логер працює з будь-яким бекендом без вибуху класів — саме те, що треба.

## Що окремо круто

* Чіткий контракт `LogBackend.write / writeBatch`.
* Три різні поведінки логера (sync / buffered / async).
* S3 правильно приймає батчі.

## Що підкрутити

1. **Зробити `Logger` абстракцією, не конкретним класом**
   Назви його `BaseLogger` (abstract) або зроби інтерфейс з дефолтними `info/error`. Так зручніше додавати нові логери.

   ```java
   public interface Logger {
     void log(String level, String msg);
     default void info(String msg){ log("INFO", msg); }
     default void error(String msg){ log("ERROR", msg); }
   }
   ```

   Тоді `BufferedLogger`/`AsyncLogger` імплементують `Logger`, а не наслідуються.

2. **Batch у `BufferedLogger.flush()`**
   Зараз ти ітеруєш `write` по одному. Краще разом:

   ```java
   public void flush() {
     if (buf.isEmpty()) return;
     backend.writeBatch(new ArrayList<>(buf));
     buf.clear();
   }
   ```

   І подумай про потокобезпеку (або документуй “single-threaded use”). Для multi-thread — synchronized/lock або `ConcurrentLinkedQueue` + атомарний дренаж.

3. **Кероване завершення `AsyncLogger`**
   Додай `close()`/`shutdown()` з флашем батча й коректною зупинкою воркера (і перейменуй тред: зараз `"s3-logger"`, хоч бекенд може бути будь-який).

   ```java
   public final class AsyncLogger implements Logger, AutoCloseable {
     private final ExecutorService exec = Executors.newSingleThreadExecutor(r -> new Thread(r,"async-logger"));
     private final BlockingQueue<LogEvent> q = new LinkedBlockingQueue<>();
     private volatile boolean running = true;

     public AsyncLogger(LogBackend backend, int maxBatch, Duration wait) {
       exec.submit(() -> {
         var batch = new ArrayList<LogEvent>(maxBatch);
         while (running || !q.isEmpty()) {
           LogEvent e = q.poll(wait.toMillis(), TimeUnit.MILLISECONDS);
           if (e!=null) batch.add(e);
           if (batch.size()>=maxBatch || (e==null && !batch.isEmpty())) {
             backend.writeBatch(new ArrayList<>(batch));
             batch.clear();
           }
         }
       });
     }
     @Override public void log(String level, String msg){ q.offer(new LogEvent(level, msg)); }
     @Override public void close() {
       running = false; exec.shutdown();
       try { exec.awaitTermination(2, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}
     }
   }
   ```

4. **Не робити поля бекендів статичними**
   `ConsoleBackend/KafkaBackend/S3Backend` краще інстансові (легше тестувати/мокати). Якщо хочеш шарити — передавай залежності через DI.

5. **Уніфікований формат (Layout) як окрема стратегія**
   Розведи форматування і транспорт:

   ```java
   public interface Layout { LogEvent format(String level, String msg); }
   public final class SimpleLayout implements Layout {
     public LogEvent format(String level, String msg){ return new LogEvent(level, msg); }
   }
   // Logger приймає Layout і перед log() форматує подію
   ```

6. **Контракт `LogBackend` зі зручними дефолтами**

   ```java
   public interface LogBackend {
     void write(LogEvent e);
     default void writeBatch(List<LogEvent> batch){ batch.forEach(this::write); }
   }
   ```

   Тоді бекенд з нативним батчем (S3) просто перевизначає `writeBatch`.

7. **Маленькі штрихи**

   * Познач поля як `final` де можливо.
   * В `Main` виклич `flush()`/`close()` (буфер/async).
   * (Опційно) зворотний тиск в `AsyncLogger`: bounded queue + `offer(..., timeout)`.

## Міні-рефреш основи (структура)

```java
// Abstraction
public interface Logger {
  void log(String level, String msg);
  default void info(String m){ log("INFO", m); }
  default void error(String m){ log("ERROR", m); }
}

public final class BasicLogger implements Logger {
  private final LogBackend backend; private final Layout layout;
  public BasicLogger(LogBackend b, Layout l){ this.backend=b; this.layout=l; }
  public void log(String level, String msg){ backend.write(layout.format(level, msg)); }
}

public final class BufferedLogger implements Logger, AutoCloseable {
  private final LogBackend backend; private final Layout layout;
  private final List<LogEvent> buf = new ArrayList<>(); private final int batchSize;
  public BufferedLogger(LogBackend b, Layout l, int n){ backend=b; layout=l; batchSize=n; }
  public synchronized void log(String level, String msg){
    buf.add(layout.format(level, msg));
    if (buf.size()>=batchSize) flush();
  }
  public synchronized void flush(){ if(!buf.isEmpty()){ backend.writeBatch(new ArrayList<>(buf)); buf.clear(); } }
  public void close(){ flush(); }
}
```
