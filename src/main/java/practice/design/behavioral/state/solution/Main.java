package practice.design.behavioral.state.solution;

import practice.design.behavioral.state.solution.exception.BusinessRuleViolation;
import practice.design.behavioral.state.solution.service.Order;

public class Main {

    public static void main(String[] args) {

        Order orderWithNext = new Order();
        orderWithNext.next();
        orderWithNext.next();
        orderWithNext.next();
        orderWithNext.next();

        System.out.println();

        Order orderDirect = new Order();
        orderDirect.pay();
        try {
            orderDirect.deliver();
        } catch (BusinessRuleViolation error) {
            System.out.println(error);
        }

        try {
            orderDirect.refund();
        } catch (BusinessRuleViolation error) {
            System.out.println(error);
        }

        orderDirect.ship();

        try {
            orderDirect.cancel();
        } catch (BusinessRuleViolation error) {
            System.out.println(error);
        }

        orderDirect.deliver();

        try {
            orderDirect.refund();
        } catch (BusinessRuleViolation error) {
            System.out.println(error);
        }
    }
}
