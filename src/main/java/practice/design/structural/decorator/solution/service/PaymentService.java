package practice.design.structural.decorator.solution.service;

import practice.design.structural.decorator.solution.model.PaymentRequest;
import practice.design.structural.decorator.solution.model.Receipt;

public interface PaymentService {
    Receipt pay(PaymentRequest req);
}
