package practice.design.behavioral.chainresponsibility.solution.model;

public class ApprovalResult {
    final String approver;
    final boolean approved;
    final String message;

    public ApprovalResult(String approver, boolean approved, String message) {
        this.approver = approver;
        this.approved = approved;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Approval{by=%s, ok=%s, msg=%s}".formatted(approver, approved, message);
    }
}
