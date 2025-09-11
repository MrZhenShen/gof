package practice.design.creational.objectPool.solution;

import java.util.Random;

public class ExpensiveConnection {
    private final int id;
    private boolean open;

    ExpensiveConnection(int id) {
        this.id = id;
        simulateHeavyInit(); // довга ініціалізація
        this.open = true;
        System.out.println("[conn#" + id + "] opened");
    }

    private void simulateHeavyInit() {
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
    }

    public void execute(String sql) {
        if (!open) throw new IllegalStateException("Connection is closed");
        System.out.println("[conn#" + id + "] exec: " + sql);
        // Умовне виконання
        try { Thread.sleep(50 + new Random().nextInt(100)); } catch (InterruptedException ignored) {}
    }

    public void close() {
        open = false;
        System.out.println("[conn#" + id + "] closed");
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
