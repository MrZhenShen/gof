## 📌 Проблема

Ти робиш графічний редактор із **фігурами** (кола, прямокутники).
Користувачі часто створюють нові фігури на основі існуючих — “таку саму, але іншим кольором/радіусом”.
Зараз копіювання робиться вручну, і виходять проблеми:

* Копіювання полів розкидане по коду й дублюється.
* Є вкладені об’єкти (наприклад, `Style`), і копії часто **ділять спільний стан** → зміни стилю в “копії” ламають оригінал.
* Додати нову фігуру = переписати купу умов і копіювань.

Потрібно централізувати й уніфікувати процес створення копій фігур так, щоб:

* легко “розмножувати” фігури як **шаблони**,
* **не** було витоків спільного стану для вкладених об’єктів (де потрібно — глибокі копії),
* клієнтський код не знав деталей копіювання.

---

## 📌 Початковий код (без патерну)

```java
// Вкладений стан, який часто шариться помилково
class Style {
    String color;
    float strokeWidth;

    Style(String color, float strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
}

// Базова фігура
abstract class Shape {
    int x, y;
    Style style;

    Shape(int x, int y, Style style) {
        this.x = x;
        this.y = y;
        this.style = style; // ⚠️ спільне посилання
    }

    abstract void draw();
}

// Конкретні фігури
class Circle extends Shape {
    int radius;

    Circle(int x, int y, int radius, Style style) {
        super(x, y, style);
        this.radius = radius;
    }

    @Override
    void draw() {
        System.out.println("Draw Circle at (" + x + "," + y + "), r=" + radius +
                ", color=" + style.color + ", stroke=" + style.strokeWidth);
    }
}

class Rectangle extends Shape {
    int width, height;

    Rectangle(int x, int y, int width, int height, Style style) {
        super(x, y, style);
        this.width = width;
        this.height = height;
    }

    @Override
    void draw() {
        System.out.println("Draw Rect at (" + x + "," + y + "), " +
                width + "x" + height + ", color=" + style.color +
                ", stroke=" + style.strokeWidth);
    }
}

// Наївний менеджер "копіювання" (насправді повторне створення)
class ShapeManager {

    // "Клонування" через ручний new + копіювання полів
    Circle duplicateCircle(Circle src) {
        // ⚠️ style передається як є (спільний стан!)
        return new Circle(src.x, src.y, src.radius, src.style);
    }

    Rectangle duplicateRectangle(Rectangle src) {
        return new Rectangle(src.x, src.y, src.width, src.height, src.style);
    }
}

public class Main {
    public static void main(String[] args) {
        Style style = new Style("red", 2.0f);

        Circle original = new Circle(10, 10, 20, style);
        Circle copy = new ShapeManager().duplicateCircle(original);

        original.draw();
        copy.draw();

        // Змінимо стиль у "копії" — і це зламає оригінал (спільний Style)
        copy.style.color = "blue";
        copy.style.strokeWidth = 5.0f;

        System.out.println("-- after copy style change --");
        original.draw(); // ❌ теж стане синім
        copy.draw();
    }
}
```

---

## 🎯 Завдання

Перепроєктуй цей код, застосувавши **один зі створювальних патернів GoF** (який я обрав).
Вимоги до твого рішення:

* Жодних розгалужень у стилі `if (shape instanceof ...)` для копіювання.
* Копіювання інкапсульовано в самих продуктах; клієнт лише просить “ще один такий”.
* Для вкладених об’єктів (`Style`) забезпеч правильну семантику копії (у нашому випадку — окремий екземпляр).
* Має бути зручно тримати **шаблони фігур** і створювати з них нові екземпляри.
