package practice.design.structural.decorator.solution.service.decorator;

import practice.design.structural.decorator.solution.service.PaymentService;
import practice.design.structural.decorator.solution.model.PaymentRequest;
import practice.design.structural.decorator.solution.model.Receipt;
import practice.design.structural.decorator.solution.service.store.IdempotencyStore;

public class IdempotencyDecorator implements PaymentService {
    private final PaymentService delegate;
    private final IdempotencyStore idempotencyStore;

    public IdempotencyDecorator(PaymentService decorator, IdempotencyStore idempotencyStore) {
        this.delegate = decorator;
        this.idempotencyStore = idempotencyStore;
    }

    @Override
    public Receipt pay(PaymentRequest req) {
        if (req.idempotencyKey() != null && !idempotencyStore.addKey(req.idempotencyKey())) {
            return new Receipt(req.id(), true, "Already processed");
        }
        return delegate.pay(req);

    }
}
