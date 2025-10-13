package practice.design.behavioral.mediator.raw;

import java.util.ArrayList;
import java.util.List;

class User {
    private final String name;
    private final List<User> others = new ArrayList<>();

    public User(String name) { this.name = name; }

    public void connect(User other) { others.add(other); }

    public void send(String message) {
        System.out.println(name + " sends: " + message);
        for (User u : others) {
            u.receive(message, name);
        }
    }

    public void receive(String message, String from) {
        System.out.println("  " + name + " got from " + from + ": " + message);
    }
}

public class Main {
    public static void main(String[] args) {
        User alice = new User("Alice");
        User bob   = new User("Bob");
        User carol = new User("Carol");

        // Прямі посилання
        alice.connect(bob);
        alice.connect(carol);
        bob.connect(alice);
        carol.connect(alice);

        alice.send("Hello team!");
        bob.send("Hi Alice!");
    }
}