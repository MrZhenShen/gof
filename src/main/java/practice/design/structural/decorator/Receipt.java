package practice.design.structural.decorator;

import java.time.Instant;

public class Receipt {
    final String paymentId;
    final boolean success;
    final String message;
    final Instant at = Instant.now();

    Receipt(String paymentId, boolean success, String message) {
        this.paymentId = paymentId;
        this.success = success;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Receipt{id=%s, success=%s, msg=%s, at=%s}".formatted(paymentId, success, message, at);
    }
}
