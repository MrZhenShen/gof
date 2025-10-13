package practice.design.behavioral.command.solution.command;

import practice.design.behavioral.command.solution.service.Remote;

public abstract class Command<T> {
    public Remote remote;
    public T backup;

    public Command(Remote remote) {
        this.remote = remote;
    }

    public abstract void execute();

    public abstract void undo();

    void backup(T value) {
        backup = value;
    }
}
