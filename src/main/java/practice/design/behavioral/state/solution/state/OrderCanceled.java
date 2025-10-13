package practice.design.behavioral.state.solution.state;

import practice.design.behavioral.state.solution.service.Order;

public class OrderCanceled extends OrderState {

    public OrderCanceled(Order order) {
        super(order);
    }

    @Override
    public void next() {
        notifyUnavailableAction();
    }

    @Override
    public String toString() {
        return "Canceled";
    }
}
