package practice.design.behavioral.chainresponsibility.solution.service.approvalChain;

import practice.design.behavioral.chainresponsibility.solution.model.ApprovalResult;
import practice.design.behavioral.chainresponsibility.solution.model.ExpenseRequest;

public abstract class Approval {

    private final Approval higherApproval;

    Approval(Approval higherApproval) {
        this.higherApproval = higherApproval;
    }

    abstract ApprovalResult handle(ExpenseRequest r);

    ApprovalResult handlePreconditionOrDelegate(ExpenseRequest r, boolean condition, ApprovalResult truthyResult) {
        return condition ? truthyResult : delegate(r);
    }

    ApprovalResult delegate(ExpenseRequest r) {
        return higherApproval.handle(r);
    }
}