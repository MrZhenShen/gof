package practice.design.behavioral.state.solution.state;

import practice.design.behavioral.state.solution.service.Order;

public class OrderRefunded extends OrderState {

    public OrderRefunded(Order order) {
        super(order);
    }

    @Override
    public void next() {
        notifyUnavailableAction();
    }

    @Override
    public String toString() {
        return "Refunded";
    }
}