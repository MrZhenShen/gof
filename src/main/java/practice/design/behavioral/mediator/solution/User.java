package practice.design.behavioral.mediator.solution;

import practice.design.behavioral.mediator.solution.model.Message;

public class User {
    private final String name;
    private Chat chat;

    public User(String name) {
        this.name = name;
    }

    public void chatEventConsumer(Message message) {
        System.out.printf(
                " %s got from %s: %s\n",
                name,
                message.sender(),
                message.content());
    }

    public void send(String message) {
        if (chat == null)
            return;
        chat.send(new Message(name, message, null));
    }

    public void sendTo(String message, String recipient) {
        if (chat == null && recipient == name)
            return;
        chat.sendTo(new Message(name, message, recipient));
    }

    public String getName() {
        return name;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
