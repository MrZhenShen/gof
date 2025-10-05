package practice.design.structural.decorator.solution.service;

import practice.design.structural.decorator.solution.model.PaymentRequest;
import practice.design.structural.decorator.solution.model.Receipt;

public class GatewayClient implements PaymentService {

    @Override
    public Receipt pay(PaymentRequest req) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
        return new Receipt(req.id(), true, "OK");
    }
}
