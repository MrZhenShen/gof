package practice.design.behavioral.command.service;

import java.util.Stack;

import practice.design.behavioral.command.command.Command;

public class CommandHistory {
    private Stack<Command<?>> history = new Stack<>();
    private Stack<Command<?>> futures = new Stack<>();

    public void push(Command<?> c) {
        history.push(c);
    }

    public Command<?> pop() {
        Command<?> poped = history.pop();
        futures.push(poped);

        return poped;
    }

    public Command<?> popFuture() {
        return futures.pop();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public boolean noFuture() {
        return futures.isEmpty();
    }
}
