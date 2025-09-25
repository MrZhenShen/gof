package practice.design.structural.decorator;

public class Main {

    public static void main(String[] args) {
        PaymentService base = new GatewayClient();

        IdempotencyStore store = new IdempotencyStore();

        PaymentService svc =
                new MetricsDecorator(
                        new AuditDecorator(
                                new FraudCheckDecorator(
                                        new IdempotencyDecorator(base, store)
                                )
                        )
                );

        Receipt r1 = svc.pay(new PaymentRequest("p-1", "idem-1", 1299, "USD"));
        Receipt r2 = svc.pay(new PaymentRequest("p-2", "idem-1", 1299, "USD"));

        System.out.println(r1);
        System.out.println(r2);
    }
}
