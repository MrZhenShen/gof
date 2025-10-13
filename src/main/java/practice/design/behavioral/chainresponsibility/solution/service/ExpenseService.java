package practice.design.behavioral.chainresponsibility.solution.service;

import practice.design.behavioral.chainresponsibility.solution.model.ApprovalResult;
import practice.design.behavioral.chainresponsibility.solution.model.ExpenseRequest;
import practice.design.behavioral.chainresponsibility.solution.service.approvalChain.TeamLeadApproval;

public class ExpenseService {
    public ApprovalResult approve(ExpenseRequest r) {
        return new TeamLeadApproval().handle(r);
    }
}
