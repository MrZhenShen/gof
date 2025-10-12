package practice.design.behavioral.mediator.solution;

public class Main {
    public static void main(String[] args) {

        Chat schoolGroup = new Chat();

        User alice = new User("Alice");
        User carol = new User("Carol");
        User bob = new User("Bob");
        User andrew = new User("Andrew");

        schoolGroup.addMember(alice);
        schoolGroup.addMember(carol);
        schoolGroup.addMember(bob);

        schoolGroup.broadcastSystem("Support time 1am - 2am");

        alice.send("Hello team!");
        bob.sendTo("Hello Alice!", "Alice");

        andrew.send("Hello from back door");
    }
}
