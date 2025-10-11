package practice.design.behavioral.state.solution.state;

import practice.design.behavioral.state.solution.service.Order;

public class OrderNew extends OrderState {

    public OrderNew(Order order) {
        super(order);
    }

    @Override
    public void next() {
        pay();
    }

    @Override
    public void pay() {
        order.changeState(new OrderPaid(order));
    }

    @Override
    public void cancel() {
        order.changeState(new OrderCanceled(order));
    }

    @Override
    public String toString() {
        return "New";
    }
}