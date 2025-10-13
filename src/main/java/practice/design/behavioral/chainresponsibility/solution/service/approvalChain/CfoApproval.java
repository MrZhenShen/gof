package practice.design.behavioral.chainresponsibility.solution.service.approvalChain;

import practice.design.behavioral.chainresponsibility.solution.model.ApprovalResult;
import practice.design.behavioral.chainresponsibility.solution.model.ExpenseRequest;

public class CfoApproval extends Approval {

    public CfoApproval() {
        super(null);
    }

    @Override
    public ApprovalResult handle(ExpenseRequest request) {
        return new ApprovalResult("CFO", true, "OK > $20k");
    }
}
