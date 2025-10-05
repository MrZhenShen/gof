package practice.design.structural.decorator.solution.service.decorator;

import practice.design.structural.decorator.solution.service.PaymentService;
import practice.design.structural.decorator.solution.model.PaymentRequest;
import practice.design.structural.decorator.solution.model.Receipt;

public class AuditDecorator implements PaymentService {
    private final PaymentService delegate;

    public AuditDecorator(PaymentService decorator) {
        this.delegate = decorator;
    }

    @Override
    public Receipt pay(PaymentRequest req) {
        printAudit("start " + req.id() + " " + req.amountCents() + req.currency());

        Receipt receipt = delegate.pay(req);

        printAudit("done " + req.id() + " -> " + receipt.isSuccess());
        return receipt;
    }

    public void printAudit(String message) {
        System.out.println("[AUDIT] " + message);
    }
}
