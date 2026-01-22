## Task

You’re building a service that processes bursts of short-lived jobs (think: generating thumbnails, compiling templates, running a report query). Each job needs an **expensive-to-create worker object**:

* Creating a worker takes **~200ms** (simulated) and allocates memory.
* A worker must be **“reset”** after each use (jobs must not leak state into other jobs).
* At peak, you may have **100+ jobs**, but you must cap the number of workers to **N** (e.g., 4 or 8) because they wrap a limited resource (native handle / GPU context / external session).
* Jobs can run concurrently (multiple threads).
* If no worker is available, the job should **wait up to a timeout**, then fail fast with a clear error.
* You want to **avoid constantly constructing/destroying workers** and reduce GC pressure.

Your job: refactor the starter code so it meets the constraints.

---

## Starter code

### `Worker.java`

```java
package training.pool;

import java.util.concurrent.atomic.AtomicInteger;

public final class Worker {
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    private final int id;
    private String lastJobName;
    private int processed;

    public Worker() {
        this.id = SEQ.incrementAndGet();
        // Simulate expensive creation
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Created Worker #" + id);
    }

    public void process(String jobName) {
        // Simulate some stateful work
        this.lastJobName = jobName;
        this.processed++;
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("Worker #%d processed '%s' (count=%d)%n", id, jobName, processed);
    }

    public void reset() {
        // Required between jobs to avoid state leaks
        lastJobName = null;
    }

    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return "Worker{id=" + id + ", processed=" + processed + ", lastJobName=" + lastJobName + "}";
    }
}
```

### `JobService.java`

```java
package training.pool;

public class JobService {

    // Naive: creates a new Worker per job -> slow and wasteful
    public void handle(String jobName) {
        Worker w = new Worker();
        try {
            w.process(jobName);
        } finally {
            // Nothing reusable happens here; reset is useless because we discard the instance
            w.reset();
        }
    }
}
```

### `Main.java`

```java
package training.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int threads = 12;
        int jobs = 40;

        ExecutorService exec = Executors.newFixedThreadPool(threads);
        JobService service = new JobService();

        List<Future<?>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 1; i <= jobs; i++) {
            String name = "job-" + i;
            futures.add(exec.submit(() -> service.handle(name)));
        }

        for (Future<?> f : futures) f.get();
        exec.shutdown();

        long took = System.currentTimeMillis() - start;
        System.out.println("\nDONE in " + took + " ms");
    }
}
```

---

## What you need to deliver

Refactor so that:

1. Worker creation is **bounded** (max `POOL_SIZE` workers).
2. Jobs **reuse** workers rather than creating new ones.
3. Safe for **concurrent use**.
4. If pool is empty, acquisition **waits up to `timeout`** then throws (or returns failure).
5. Every returned worker is **reset** before being reused.
6. The `JobService` stays clean: it shouldn’t know *how* workers are managed beyond “borrow/use/return”.

---

## Constraints (to keep the exercise honest)

* Keep `Worker` mostly intact (you may add small helpers but don’t remove the expensive constructor sleep).
* Don’t cheat by “making Worker cheap”.
* Don’t solve by using a single global Worker with `synchronized process()` (that kills concurrency and misses the point).
* Prefer standard JDK concurrency utilities.
