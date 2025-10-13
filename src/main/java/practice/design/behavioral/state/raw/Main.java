package practice.design.behavioral.state.raw;

enum Status { NEW, PAID, SHIPPED, DELIVERED, CANCELLED }

class BusinessRuleViolation extends RuntimeException {
    BusinessRuleViolation(String msg) { super(msg); }
}

class Order {
    private Status status = Status.NEW;

    public Status getStatus() { return status; }

    public void pay() {
        switch (status) {
            case NEW -> status = Status.PAID;
            case PAID, SHIPPED, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Pay not allowed in " + status);
        }
        System.out.println("[pay] -> " + status);
    }

    public void ship() {
        switch (status) {
            case PAID -> status = Status.SHIPPED;
            case NEW, SHIPPED, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Ship not allowed in " + status);
        }
        System.out.println("[ship] -> " + status);
    }

    public void deliver() {
        switch (status) {
            case SHIPPED -> status = Status.DELIVERED;
            case NEW, PAID, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Deliver not allowed in " + status);
        }
        System.out.println("[deliver] -> " + status);
    }

    public void cancel() {
        switch (status) {
            case NEW, PAID -> status = Status.CANCELLED;
            case SHIPPED, DELIVERED, CANCELLED -> throw new BusinessRuleViolation("Cancel not allowed in " + status);
        }
        System.out.println("[cancel] -> " + status);
    }
}

public class Main {
    public static void main(String[] args) {
        Order o = new Order();
        o.pay();
        o.ship();
        o.deliver();

        // спроба некоректної операції:
        try { o.cancel(); } catch (BusinessRuleViolation ex) { System.out.println(ex.getMessage()); }
    }
}
