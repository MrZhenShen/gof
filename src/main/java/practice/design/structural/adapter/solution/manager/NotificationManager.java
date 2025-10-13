package practice.design.structural.adapter.solution.manager;

import practice.design.structural.adapter.solution.exception.NotifierNotFoundException;
import practice.design.structural.adapter.solution.notifier.Notifier;

public class NotificationManager {
    
    private Notifier notifier;

    public NotificationManager() {}

    public NotificationManager(Notifier notifier) {
        this.notifier = notifier;
    }

    public NotificationManager setNotifier(Notifier notifier) {
        this.notifier = notifier;
        return this;
    }
    
    public NotificationManager notify(String recipient, String message) throws NotifierNotFoundException {
        if(this.notifier == null) {
            throw new NotifierNotFoundException();
        }
        this.notifier.send(recipient, message);
        return this;
    }
}

