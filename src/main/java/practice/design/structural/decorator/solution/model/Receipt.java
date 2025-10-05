package practice.design.structural.decorator.solution.model;

import java.time.Instant;

public class Receipt {
    private final String paymentId;
    private final boolean success;
    private final String message;
    private final Instant at = Instant.now();

    public Receipt(String paymentId, boolean success, String message) {
        this.paymentId = paymentId;
        this.success = success;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Receipt{id=%s, success=%s, msg=%s, at=%s}".formatted(paymentId, success, message, at);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Instant getAt() {
        return at;
    }
}
