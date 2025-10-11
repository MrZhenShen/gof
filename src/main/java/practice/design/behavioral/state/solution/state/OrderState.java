package practice.design.behavioral.state.solution.state;

import practice.design.behavioral.state.solution.exception.BusinessRuleViolation;
import practice.design.behavioral.state.solution.service.Order;

public abstract class OrderState {
    protected Order order;

    public OrderState(Order order) {
        this.order = order;
    }

    public abstract void next();

    public void pay() {
        notifyUnavailableAction("Pay");
    };

    public void refund() {
        notifyUnavailableAction("Refund");
    };

    public void ship() {
        notifyUnavailableAction("Shipping");
    };

    public void deliver() {
        notifyUnavailableAction("Delivery");
    };

    public void cancel() {
        notifyUnavailableAction("Cancelation");
    };

    void notifyUnavailableAction(String action) {
        throw new BusinessRuleViolation(String.format("%s is not available when %s", action, order.getStatus()));
    }

    void notifyUnavailableAction() {
        notifyUnavailableAction("Action");
    }
}
