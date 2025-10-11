package practice.design.behavioral.chainresponsibility.solution.service.approvalChain;

import practice.design.behavioral.chainresponsibility.solution.model.ApprovalResult;
import practice.design.behavioral.chainresponsibility.solution.model.Category;
import practice.design.behavioral.chainresponsibility.solution.model.ExpenseRequest;

public class TeamLeadApproval extends Approval {

    public TeamLeadApproval() {
        super(new ManagerApproval());
    }

    @Override
    public ApprovalResult handle(ExpenseRequest request) {
        return handlePreconditionOrDelegate(
                request,
                request.amountCents() <= 50_000 && request.category() != Category.TRAVEL,
                new ApprovalResult("TeamLead", true, "OK up to $500"));
    }
}
