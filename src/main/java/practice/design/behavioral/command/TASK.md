## 📌 Проблема

У тебе “розумний дім” із пристроями: `Light`, `Thermostat`, `GarageDoor`.
Є клас `Remote`, у якого 3 кнопки. Зараз кожна кнопка **жорстко прив’язана** до конкретного пристрою та дії. Наслідки:

* Щоб змінити призначення кнопки — треба міняти код `Remote`.
* Немає **Undo/Redo**.
* Немає **макрокоманд** (послідовність дій на одну кнопку).
* Неможливо логувати чи планувати виконання, не чіпаючи бізнес-логіку пристроїв.

Потрібно відв’язати кнопки від конкретних дій і навчити пульт працювати з діями як з об’єктами.

---

## 📌 Початковий код (без патерну)

```java
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
```

---

## 🎯 Завдання

Вимоги до твого рішення:

1. Кнопки пульта мають приймати **об’єкти-дії**, а не знати про конкретні пристрої.
2. Додай **undo** для кожної дії (і, за бажанням, **redo** через дві стекові структури).
3. Підтримай **макрокоманди**: одна кнопка виконує список дій послідовно (і вміє відкотити їх у зворотному порядку).
4. Покажи в `Main` як:

   * перепризначити кнопки без зміни класу `Remote`,
   * виконати макрокоманду (“Нічний режим”: вимкнути світло, закрити гараж, поставити 18°),
   * зробити кілька `undo()` підряд.

> Бонус (не обов’язково): додай легке **логування** виконання команд у центральному місці (не в пристроях).
