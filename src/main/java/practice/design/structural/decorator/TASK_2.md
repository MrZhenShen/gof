
## 📌 Нова постановка (Decorator, але не туторіал)

Є базова служба `PaymentService`, яка відправляє платежі у зовнішній шлюз.
Додаткові вимоги, які **інколи** треба вмикати:

* **Idempotency**: не проводити повторно той самий платіж (по `idempotencyKey`).
* **Fraud check**: простий скоринг та можливість відхилити платіж.
* **Audit log**: журналюємо вхід/вихід, але не чіпаємо бізнес-логіку.
* **Metrics**: обгорнути виконання таймером та інкрементити лічильники.

Клієнти хочуть мати змогу **компонувати** ці можливості довільно й у довільному порядку (наприклад, `new Metrics(new Audit(new FraudCheck(new Idempotency(base))))`).
Базовий сервіс не повинен знати про жодну з цих надбудов.

---

## 📌 Початковий код (без патерну, “антиприклад” із прапорцями)

```java
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class PaymentRequest {
    final String id;              // payment id
    final String idempotencyKey;  // може бути null
    final long amountCents;
    final String currency;

    PaymentRequest(String id, String idempotencyKey, long amountCents, String currency) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.amountCents = amountCents;
        this.currency = currency;
    }
}

class Receipt {
    final String paymentId;
    final boolean success;
    final String message;
    final Instant at = Instant.now();

    Receipt(String paymentId, boolean success, String message) {
        this.paymentId = paymentId;
        this.success = success;
        this.message = message;
    }
    @Override public String toString() {
        return "Receipt{id=%s, success=%s, msg=%s, at=%s}".formatted(paymentId, success, message, at);
    }
}

interface PaymentService {
    Receipt pay(PaymentRequest req);
}

// Наївна реалізація з прапорцями
class DefaultPaymentService implements PaymentService {
    private final boolean enableIdempotency;
    private final boolean enableFraudCheck;
    private final boolean enableAudit;
    private final boolean enableMetrics;

    // Глобальний стан у “сервісі” (погано)
    private static final Set<String> seenIdempotency = ConcurrentHashMap.newKeySet();

    DefaultPaymentService(boolean idemp, boolean fraud, boolean audit, boolean metrics) {
        this.enableIdempotency = idemp;
        this.enableFraudCheck  = fraud;
        this.enableAudit       = audit;
        this.enableMetrics     = metrics;
    }

    @Override
    public Receipt pay(PaymentRequest req) {
        long start = System.nanoTime();
        if (enableAudit) {
            System.out.println("[AUDIT] start " + req.id + " " + req.amountCents + req.currency);
        }

        if (enableIdempotency && req.idempotencyKey != null) {
            if (!seenIdempotency.add(req.idempotencyKey)) {
                if (enableAudit) System.out.println("[AUDIT] duplicate " + req.id);
                if (enableMetrics) System.out.println("[METRIC] duplicate.count += 1");
                return new Receipt(req.id, true, "Already processed");
            }
        }

        if (enableFraudCheck) {
            int score = (int) (req.amountCents % 10); // псевдоскоринг
            if (score > 7) {
                if (enableAudit) System.out.println("[AUDIT] fraud-block " + req.id);
                if (enableMetrics) System.out.println("[METRIC] fraud.blocked += 1");
                return new Receipt(req.id, false, "Fraud suspected");
            }
        }

        // “Виклик шлюзу”
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        Receipt r = new Receipt(req.id, true, "OK");

        if (enableAudit) System.out.println("[AUDIT] done " + req.id + " -> " + r.success);
        if (enableMetrics) {
            long durMs = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[METRIC] payment.latency.ms = " + durMs);
        }
        return r;
    }
}

// Демонстрація
public class Main {
    public static void main(String[] args) {
        PaymentService svc = new DefaultPaymentService(true, true, true, true);
        var r1 = svc.pay(new PaymentRequest("p-1", "idem-1", 1299, "USD"));
        var r2 = svc.pay(new PaymentRequest("p-2", "idem-1", 1299, "USD")); // дубль
        System.out.println(r1);
        System.out.println(r2);
    }
}
```

---

## 🎯 Завдання (що саме зробити)

Перепроєктуй на **Decorator**:

1. Базовий компонент:

    * `class GatewayClient implements PaymentService` — тільки реальний виклик шлюзу (тут: `Thread.sleep(50)` і повернення `Receipt("OK")`). **Без** жодних перевірок/логів/метрик.

2. Декоратори (кожен — окремий клас, що реалізує `PaymentService` і має поле `delegate`):

    * `IdempotencyDecorator`
    * `FraudCheckDecorator`
    * `AuditDecorator`
    * `MetricsDecorator`
      Кожен додає свою поведінку **до/після** `delegate.pay(req)` і не знає про решту надбудов.

3. Компонування:

    * У `Main` покажи 2–3 різні **ланцюжки** (різні порядки/набори декораторів) і їхній ефект.
    * Жодних прапорців або `instanceof` для вмикання/вимикання поведінок.

4. Додатково (опційно):

    * Винеси стан idempotency у **сховище**, що передається через конструктор декоратора (інтерфейс `IdempotencyStore` → легше тестувати).
    * У `MetricsDecorator` порахуй час і виведи latency.
    * У `AuditDecorator` логуй start/stop + результат.

Очікуваний стиль виклику у `Main` (приклад):

```java
PaymentService base = new GatewayClient();
PaymentService svc =
    new MetricsDecorator(
      new AuditDecorator(
        new FraudCheckDecorator(
          new IdempotencyDecorator(base, store)
        )
      )
    );

Receipt r = svc.pay(req);
```

```shell
[AUDIT] start p-1 1299USD
[AUDIT] fraud-block p-1
[METRIC] fraud.blocked += 1
[AUDIT] start p-2 1299USD
[AUDIT] duplicate p-2
[METRIC] duplicate.count += 1
Receipt{id=p-1, success=false, msg=Fraud suspected, at=2025-09-24T15:33:26.835647700Z}
Receipt{id=p-2, success=true, msg=Already processed, at=2025-09-24T15:33:26.835647700Z}
```