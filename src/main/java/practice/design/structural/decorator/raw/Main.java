package practice.design.structural.decorator.raw;

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
