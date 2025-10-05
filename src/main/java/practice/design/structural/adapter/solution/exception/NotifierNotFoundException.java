package practice.design.structural.adapter.solution.exception;

public class NotifierNotFoundException extends ClassNotFoundException {

    public NotifierNotFoundException() {
        super("Notifier is not set", null);
    }
}
