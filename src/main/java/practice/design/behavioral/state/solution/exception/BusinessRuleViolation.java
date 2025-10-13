package practice.design.behavioral.state.solution.exception;

public class BusinessRuleViolation extends RuntimeException {
    public BusinessRuleViolation(String msg) {
        super(msg);
    }
}
