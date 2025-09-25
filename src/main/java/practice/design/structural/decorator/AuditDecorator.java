package practice.design.structural.decorator;

public class AuditDecorator implements PaymentService {
    private final PaymentService delegate;

    public AuditDecorator(PaymentService decorator) {
        this.delegate = decorator;
    }

    @Override
    public Receipt pay(PaymentRequest req) {
        printAudit("start " + req.id + " " + req.amountCents + req.currency);

        Receipt receipt = delegate.pay(req);

        printAudit("done " + req.id + " -> " + receipt.success);
        return receipt;
    }

    public void printAudit(String message) {
        System.out.println("[AUDIT] " + message);
    }
}
