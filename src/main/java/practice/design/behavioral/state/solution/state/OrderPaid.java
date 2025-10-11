package practice.design.behavioral.state.solution.state;

import practice.design.behavioral.state.solution.service.Order;

public class OrderPaid extends OrderState {

    public OrderPaid(Order order) {
        super(order);
    }

    @Override
    public void next() {
        ship();
    }

    @Override
    public void ship() {
        order.changeState(new OrderShipped(order));
    }

    @Override
    public void refund() {
        order.changeState(new OrderRefunded(order));
    }

    @Override
    public String toString() {
        return "Paid";
    }
}
