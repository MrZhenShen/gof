package practice.design.behavioral.state.solution.state;

import practice.design.behavioral.state.solution.service.Order;

public class OrderShipped extends OrderState {

    public OrderShipped(Order order) {
        super(order);
    }

    @Override
    public void next() {
        deliver();
    }

    @Override
    public void deliver() {
        order.changeState(new OrderDelivered(order));
    }

    @Override
    public String toString() {
        return "Shipped";
    }
}
