## 📌 Проблема

Уяви, що ти розробляєш текстовий редактор. Є базовий інтерфейс `Text`, який дозволяє вивести рядок.
Зараз при додаванні нових “фіч” (наприклад, зробити текст жирним, курсивним, підкресленим, зашифрованим, додати рамку тощо) доводиться або:

* дублювати код, створюючи нові класи під кожну комбінацію (“BoldItalicUnderlineText”),
* або вставляти купу умов усередині одного класу.

Це погано масштабується.

---

## 📌 Початковий код (без патерну)

```java
interface Text {
    String render();
}

class PlainText implements Text {
    private final String content;
    public PlainText(String content) { this.content = content; }
    public String render() { return content; }
}

// ❌ Якщо хочемо і bold, і italic, доводиться писати окремі класи
class BoldText implements Text {
    private final PlainText text;
    public BoldText(PlainText text) { this.text = text; }
    public String render() { return "<b>" + text.render() + "</b>"; }
}

class ItalicText implements Text {
    private final PlainText text;
    public ItalicText(PlainText text) { this.text = text; }
    public String render() { return "<i>" + text.render() + "</i>"; }
}

public class Main {
    public static void main(String[] args) {
        PlainText hello = new PlainText("Hello");
        BoldText bold = new BoldText(hello);
        ItalicText italic = new ItalicText(hello);

        System.out.println(bold.render());
        System.out.println(italic.render());

        // ❌ Немає способу легко поєднати Bold + Italic + Underline
    }
}
```

---

## 🎯 Завдання

Перепроєктуй цей код, застосувавши **один зі структурних патернів GoF** (який я обрав).
Вимоги:

* Можливість “обгортати” будь-який `Text` у додаткову поведінку (bold, italic, underline, …) **без створення нових комбінацій класів**.
* Клієнт має мати змогу писати щось типу:

  ```java
  Text fancy = new UnderlineDecorator(new ItalicDecorator(new BoldDecorator(new PlainText("Hello"))));
  System.out.println(fancy.render()); // <u><i><b>Hello</b></i></u>
  ```
* Легка розширюваність: додати новий “декоратор” без змін у існуючих класах.
