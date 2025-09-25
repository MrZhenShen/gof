package practice.design.structural.decorator;

public class GatewayClient implements PaymentService {

    @Override
    public Receipt pay(PaymentRequest req) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
        return new Receipt(req.id, true, "OK");
    }
}
