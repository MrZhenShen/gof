package practice.design.behavioral.state.solution.state;

import practice.design.behavioral.state.solution.service.Order;

public class OrderDelivered extends OrderState {

    public OrderDelivered(Order order) {
        super(order);
    }

    @Override
    public void next() {
        System.out.println("Order is Finalized");
    }

    @Override
    public String toString() {
        return "Delivered";
    }
}
