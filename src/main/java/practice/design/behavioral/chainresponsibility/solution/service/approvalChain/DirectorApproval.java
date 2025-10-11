package practice.design.behavioral.chainresponsibility.solution.service.approvalChain;

import practice.design.behavioral.chainresponsibility.solution.model.ApprovalResult;
import practice.design.behavioral.chainresponsibility.solution.model.Category;
import practice.design.behavioral.chainresponsibility.solution.model.ExpenseRequest;

public class DirectorApproval extends Approval {

    public DirectorApproval() {
        super(new CfoApproval());
    }

    @Override
    ApprovalResult handle(ExpenseRequest request) {
        return handlePreconditionOrDelegate(
                request,
                request.category() == Category.HARDWARE
                        && request.amountCents() > 200_000 | request.amountCents() <= 2_000_000,
                new ApprovalResult("Director", true, "OK up to $20k"));
    }
}
