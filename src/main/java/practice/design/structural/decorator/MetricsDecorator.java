package practice.design.structural.decorator;

public class MetricsDecorator implements PaymentService {
    private final PaymentService delegate;

    public MetricsDecorator(PaymentService decorator) {
        this.delegate = decorator;
    }

    @Override
    public Receipt pay(PaymentRequest req) {
        long start = System.nanoTime();

        Receipt receipt = delegate.pay(req);

        long durMs = (System.nanoTime() - start) / 1_000_000;
        printMetric("payment.latency.ms = " + durMs);
        return receipt;
    }

    public void printMetric(String message) {
        System.out.println("[METRIC] " + message);
    }
}
