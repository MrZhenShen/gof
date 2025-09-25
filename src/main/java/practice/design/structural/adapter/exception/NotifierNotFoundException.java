package practice.design.structural.adapter.exception;

public class NotifierNotFoundException extends ClassNotFoundException {

    public NotifierNotFoundException() {
        super("Notifier is not set", null);
    }
}
