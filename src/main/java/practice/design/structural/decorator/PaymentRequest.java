package practice.design.structural.decorator;

public class PaymentRequest {
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
