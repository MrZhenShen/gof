package practice.design.behavioral.chainresponsibility.solution.service.approvalChain;

import practice.design.behavioral.chainresponsibility.solution.model.ApprovalResult;
import practice.design.behavioral.chainresponsibility.solution.model.ExpenseRequest;

public class ManagerApproval extends Approval {

    public ManagerApproval() {
        super(new DirectorApproval());
    }

    @Override
    public ApprovalResult handle(ExpenseRequest request) {
        return handlePreconditionOrDelegate(
                request,
                request.amountCents() <= 500_000,
                new ApprovalResult("Manager", true, "OK up to $5k"));
    }
}
