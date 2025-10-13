package practice.design.behavioral.command.solution.provider;

public class GarageDoor {
    private boolean open;

    public void open() {
        open = true;
        System.out.println("[Garage] OPEN");
    }

    public void close() {
        open = false;
        System.out.println("[Garage] CLOSE");
    }

    public boolean isOpen() {
        return open;
    }
}
