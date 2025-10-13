package practice.design.behavioral.command.raw;

// Пристрої
class Light {
    private boolean on;
    void turnOn()  { on = true;  System.out.println("[Light] ON"); }
    void turnOff() { on = false; System.out.println("[Light] OFF"); }
    boolean isOn() { return on; }
}

class Thermostat {
    private int temp = 22;
    void setTemperature(int t) { temp = t; System.out.println("[Thermostat] temp=" + temp); }
    int getTemperature() { return temp; }
}

class GarageDoor {
    private boolean open;
    void open()  { open = true;  System.out.println("[Garage] OPEN"); }
    void close() { open = false; System.out.println("[Garage] CLOSE"); }
    boolean isOpen() { return open; }
}

// Пульт — жорстко прошиті дії
class Remote {
    private final Light light = new Light();
    private final Thermostat thermostat = new Thermostat();
    private final GarageDoor garage = new GarageDoor();

    // Кнопка 1: завжди вмикає світло
    void button1() {
        if (!light.isOn()) light.turnOn();
    }

    // Кнопка 2: завжди виставляє 25°
    void button2() {
        thermostat.setTemperature(25);
    }

    // Кнопка 3: завжди відкриває гараж
    void button3() {
        if (!garage.isOpen()) garage.open();
    }
}

public class Main {
    public static void main(String[] args) {
        Remote remote = new Remote();
        remote.button1();
        remote.button2();
        remote.button3();
        // ❌ Немає undo/redo, немає можливості перепризначити кнопки без зміни коду Remote
    }
}
