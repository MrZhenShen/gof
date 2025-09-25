package practice.design.structural.decorator;

public class IdempotencyDecorator implements PaymentService {
    private final PaymentService delegate;
    private final IdempotencyStore idempotencyStore;

    public IdempotencyDecorator(PaymentService decorator, IdempotencyStore idempotencyStore) {
        this.delegate = decorator;
        this.idempotencyStore = idempotencyStore;
    }

    @Override
    public Receipt pay(PaymentRequest req) {
        if (idempotencyStore.addKey(req.idempotencyKey) && req.idempotencyKey == null) {
            return delegate.pay(req);
        }
        return new Receipt(req.id, true, "Already processed");
    }
}
