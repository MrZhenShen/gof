package practice.design.structural.decorator.solution.model;

public record PaymentRequest(String id, String idempotencyKey, long amountCents, String currency) {
}
