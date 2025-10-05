package practice.design.structural.decorator.solution.service.decorator;

import practice.design.structural.decorator.solution.service.PaymentService;
import practice.design.structural.decorator.solution.model.PaymentRequest;
import practice.design.structural.decorator.solution.model.Receipt;

public class FraudCheckDecorator implements PaymentService {
    private final PaymentService delegate;

    public FraudCheckDecorator(PaymentService decorator) {
        this.delegate = decorator;
    }

    @Override
    public Receipt pay(PaymentRequest req) {
        int score = (int) (req.amountCents() % 10);
        if (score > 7) {
            return new Receipt(req.id(), false, "Fraud suspected");
        }

        return delegate.pay(req);
    }
}
