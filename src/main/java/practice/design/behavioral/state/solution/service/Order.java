package practice.design.behavioral.state.solution.service;

import practice.design.behavioral.state.solution.state.OrderNew;
import practice.design.behavioral.state.solution.state.OrderState;

public class Order {
    private OrderState state;

    public Order() {
        state = new OrderNew(this);
    }

    public void changeState(OrderState state) {
        if (state == null) {
            System.err.println("Status set to undefined. Keep Status.");
            return;
        }
        logTransition(this.state.toString(), state.toString());
        this.state = state;
    }

    public void next() {
        state.next();
    }

    public void cancel() {
        state.cancel();
    }

    public void pay() {
        state.pay();
    }

    public void refund() {
        state.refund();
    }

    public void ship() {
        state.ship();
    }

    public void deliver() {
        state.deliver();
    }

    public String getStatus() {
        return state.toString();
    }

    private void logTransition(String from, String to) {
        System.out.printf("Order transition: %s -> %s\n", from, to);
    }
}
