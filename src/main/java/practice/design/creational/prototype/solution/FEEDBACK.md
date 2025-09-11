🔥 Це саме **prototype** — і ти його реалізував правильно по суті ✅
Клонування інкапсульоване в самих продуктах (`duplicate()`), клієнт просто просить “ще один такий”, а вкладений стан (`Style`) не шариться з оригіналом. Красиво!

## Що зроблено добре

* **Копіювальні конструктори** (`Circle(Circle)`, `Rectangle(Rectangle)`) + `duplicate()` з **коваріантним** типом повернення — зручно й безпечніше за `clone()`.
* **Deep copy стилю**: у підкласах ти робиш `setStyle(rectangle.getStyle())`, а `getStyle()` повертає копію — тож копія не ділить `Style` з оригіналом. 👍
* Клієнтський код простий: `Circle copy = original.duplicate();`.

## Дрібні шорохи та як підсилити

1. **Deep copy – у базовому копіювальному конструкторі**
   Зараз `Shape(Shape shape)` робить **shallow** для `style`:

   ```java
   this.style = shape.style; // shallow
   ```

   А потім підкласи “виправляють” це, знову викликаючи `setStyle(...)`. Краще зробити deep copy одразу в `Shape` і **зняти дублювання** у підкласах:

   ```java
   Shape(Shape shape) {
       this.x = shape.x;
       this.y = shape.y;
       this.style = (shape.style != null) ? new Style(shape.style) : null; // deep
   }
   // тоді з Circle/Rectangle прибираємо другий setStyle(...)
   ```

2. **Однотипні модифікатори доступу**

    * У базовому класі `abstract void draw(); abstract Shape duplicate();` мають **package-private** доступ.
    * У `Circle` — `public`; у `Rectangle` — дефолтний (package-private).
      Краще зробити послідовно **public** у всіх:

   ```java
   public abstract void draw();
   public abstract Shape duplicate();
   // ...
   @Override public Rectangle duplicate() { return new Rectangle(this); }
   ```

3. **Захист інкапсуляції для style**
   Ти вже робиш копію в `getStyle()` — це добре, але тоді:

    * у `setStyle(...)` теж варто робити **defensive copy**, щоб не втащити зовнішнє посилання:

      ```java
      public void setStyle(Style style) {
          this.style = (style != null) ? new Style(style) : null;
      }
      ```
    * і, щоб було зрозуміліше, можна перейменувати `getStyle()` у `getStyleCopy()` (або документувати, що це копія). Інакше кожен виклик `draw()` створюватиме новий `Style`. Альтернатива — зробити `getStyle()` “звичайним” (повертає посилання), але тоді зберегти безпеку через immutability `Style`.

4. **Style як іммутабельний** (опційно)
   Зробити `Style` іммутабельним (final-поля, без сеттерів) — і проблема шерингу зникає:

   ```java
   public final class Style {
       private final String color;
       private final float strokeWidth;
       // конструктори + геттери, без сеттерів
       public Style withColor(String c) { return new Style(c, this.strokeWidth); }
       public Style withStroke(float s) { return new Style(this.color, s); }
   }
   ```

5. **Реєстр прототипів (шаблонів)**
   Щоб показати силу prototype, додай сховище шаблонів:

   ```java
   final class prototypeRegistry {
       private final Map<String, Shape> templates = new HashMap<>();
       public void register(String key, Shape proto) { templates.put(key, proto); }
       public Shape create(String key) {
           Shape proto = templates.get(key);
           if (proto == null) throw new IllegalArgumentException("No proto: " + key);
           return proto.duplicate();
       }
   }

   // Використання:
   prototypeRegistry reg = new prototypeRegistry();
   Circle circle = new Circle();
   circle.setX(10); circle.setY(10); circle.setRadius(20); circle.setStyle(new Style("red", 2f));
   reg.register("circle.red.20", circle);

   Shape s1 = reg.create("circle.red.20");
   s1.draw();
   ```

   Так легко “тиражувати” заготовки і підмінювати їх конфігурації централізовано.

---

## Невеликий рефакторинг-фрагмент (із виправленнями)

```java
public abstract class Shape {
    private int x, y;
    private Style style;

    protected Shape() {}

    protected Shape(Shape other) {
        this.x = other.x;
        this.y = other.y;
        this.style = other.style != null ? new Style(other.style) : null; // deep copy тут
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public Style getStyle() { return style != null ? new Style(style) : null; } // копія
    public void setStyle(Style style) { this.style = style != null ? new Style(style) : null; } // defensive copy

    public abstract void draw();
    public abstract Shape duplicate();
}

public class Circle extends Shape {
    private int radius;

    public Circle() {}
    public Circle(Circle other) {
        super(other);                 // deep у базовому конструкторі
        this.radius = other.radius;
    }

    @Override public void draw() {
        Style s = getStyle(); // копія — ок
        System.out.println("Draw Circle at (" + getX() + "," + getY() + "), r=" + radius +
                ", color=" + (s != null ? s.getColor() : "n/a") + ", stroke=" + (s != null ? s.getStrokeWidth() : 0));
    }

    @Override public Circle duplicate() { return new Circle(this); }

    // getters/setters for radius...
}
```

---

## Висновок

* Патерн **prototype** впізнається й працює правильно ✅
* Пара невеликих штрихів (deep copy у `Shape`, вирівняти доступи, defensive copy у `setStyle`, опційний реєстр прототипів) зроблять рішення “production-grade”.
