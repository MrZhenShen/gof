package practice.design.behavioral.chainresponsibility.raw;

import java.time.Instant;

enum Category { GENERAL, TRAVEL, HARDWARE }

record ExpenseRequest(String id, long amountCents, Category category, String note, Instant createdAt) {
    ExpenseRequest(String id, long amountCents, Category category, String note) {
        this(id, amountCents, category, note, Instant.now());
    }
}

class ApprovalResult {
    final String approver;
    final boolean approved;
    final String message;
    ApprovalResult(String approver, boolean approved, String message) {
        this.approver = approver; this.approved = approved; this.message = message;
    }
    @Override public String toString() {
        return "Approval{by=%s, ok=%s, msg=%s}".formatted(approver, approved, message);
    }
}

class ExpenseService {
    ApprovalResult approve(ExpenseRequest r) {
        // ❌ велика розгалужена логіка
        if (r.amountCents() <= 50_000) { // <= $500
            if (r.category() == Category.TRAVEL) {
                // travel не можна TL — передаємо вище
            } else {
                return new ApprovalResult("TeamLead", true, "OK up to $500");
            }
        }
        if (r.amountCents() <= 500_000) { // <= $5,000
            return new ApprovalResult("Manager", true, "OK up to $5k");
        }
        if (r.category() == Category.HARDWARE && r.amountCents() > 200_000) { // > $2,000
            // хочемо Director мінімум
        }
        if (r.amountCents() <= 2_000_000) { // <= $20,000
            return new ApprovalResult("Director", true, "OK up to $20k");
        }
        // все інше — CFO
        return new ApprovalResult("CFO", true, "OK > $20k");
    }
}

public class Main {
    public static void main(String[] args) {
        var svc = new ExpenseService();
        System.out.println(svc.approve(new ExpenseRequest("e-1", 35_000, Category.GENERAL, "Team offsite snacks"))); // ~ $350
        System.out.println(svc.approve(new ExpenseRequest("e-2", 120_000, Category.TRAVEL, "Flights")));              // $1,200
        System.out.println(svc.approve(new ExpenseRequest("e-3", 450_000, Category.HARDWARE, "New laptops")));       // $4,500
        System.out.println(svc.approve(new ExpenseRequest("e-4", 2_500_000, Category.GENERAL, "Booth")));            // $25,000
    }
}
