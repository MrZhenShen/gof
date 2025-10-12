package practice.design.behavioral.mediator.solution;

import java.util.HashSet;
import java.util.Set;

import practice.design.behavioral.mediator.solution.model.Message;

public class Chat {
    private final Set<User> members = new HashSet<>();
    private static final String SYSTEM_SENDER = "System";

    public void addMember(User newMember) {
        members.add(newMember);
        newMember.setChat(this);
    }

    public void send(Message message) {
        logMessage(message.sender(), message.content());

        members.forEach(member -> {
            if (member.getName() != message.sender())
                member.chatEventConsumer(message);
        });
    }

    public void sendTo(Message message) {
        logMessage(message.sender(), message.content());

        for (User member : members) {
            if (member.getName() == message.consumer()) {
                member.chatEventConsumer(message);
                return;
            }
        }
    }

    public void broadcastSystem(String messageContent) {
        logMessage(SYSTEM_SENDER, messageContent);
        members.forEach(
                member -> member.chatEventConsumer(new Message(SYSTEM_SENDER, messageContent, member.getName())));
    }

    private void logMessage(String sender, String messageContent) {
        System.out.println(sender + " sends: " + messageContent);
    }
}
