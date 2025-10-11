package practice.design.behavioral.chainresponsibility.solution.model;

import java.time.Instant;

public record ExpenseRequest(String id, long amountCents, Category category, String note, Instant createdAt) {
    public ExpenseRequest(String id, long amountCents, Category category, String note) {
        this(id, amountCents, category, note, Instant.now());
    }
}
