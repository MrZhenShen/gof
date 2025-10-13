package practice.design.structural.decorator.solution;

import practice.design.structural.decorator.solution.model.PaymentRequest;
import practice.design.structural.decorator.solution.model.Receipt;
import practice.design.structural.decorator.solution.service.GatewayClient;
import practice.design.structural.decorator.solution.service.PaymentService;
import practice.design.structural.decorator.solution.service.decorator.*;
import practice.design.structural.decorator.solution.service.store.IdempotencyStore;

public class Main {

    public static void main(String[] args) {
        PaymentService base = new GatewayClient();

        IdempotencyStore store = new IdempotencyStore();

        execIdempotencyFirst(base, store);
        execIdempotencyLast(base, store);
    }

    private static void execIdempotencyFirst(PaymentService base, IdempotencyStore store) {
        PaymentService svc =
                new IdempotencyDecorator(
                        new MetricsDecorator(
                                new AuditDecorator(
                                        new FraudCheckDecorator(base)
                                )
                        ),
                        store
                );

        Receipt r1 = svc.pay(new PaymentRequest("p-1", "idem-1", 1299, "USD"));
        Receipt r2 = svc.pay(new PaymentRequest("p-2", "idem-1", 1299, "USD"));

        System.out.println(r1);
        System.out.println(r2);
    }

    private static void execIdempotencyLast(PaymentService base, IdempotencyStore store) {
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
