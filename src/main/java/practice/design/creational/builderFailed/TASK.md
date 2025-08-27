## 📌 Проблема

Ти розробляєш систему для **побудови складних звітів**.
Звіт може складатися з таких частин:

* **Заголовок**
* **Тіло** (основний текст)
* **Футер** (підпис, дата тощо)

Клієнт може замовити різні формати звітів:

* **Простий текстовий звіт**
* **HTML-звіт**
* **PDF-звіт** (поки що лише імітація)

У поточному коді створення звіту виглядає дуже незграбно: багато умовних операторів, усі частини збираються “вручну”, логіка дублюється.

---

## 📌 Початковий код (без патерну)

```java
class Report {
    private String header;
    private String body;
    private String footer;

    public Report(String header, String body, String footer) {
        this.header = header;
        this.body = body;
        this.footer = footer;
    }

    public String getContent() {
        return header + "\n" + body + "\n" + footer;
    }
}

class ReportGenerator {
    public Report generate(String type, String title, String content) {
        if (type.equalsIgnoreCase("text")) {
            return new Report("=== " + title + " ===",
                    content,
                    "--- end of report ---");
        } else if (type.equalsIgnoreCase("html")) {
            return new Report("<h1>" + title + "</h1>",
                    "<p>" + content + "</p>",
                    "<hr><i>End of report</i>");
        } else if (type.equalsIgnoreCase("pdf")) {
            return new Report("[PDF HEADER: " + title + "]",
                    "[PDF BODY: " + content + "]",
                    "[PDF FOOTER]");
        }
        throw new IllegalArgumentException("Unknown report type: " + type);
    }
}

public class Main {
    public static void main(String[] args) {
        ReportGenerator generator = new ReportGenerator();

        Report textReport = generator.generate("text", "Sales Q1", "Some data...");
        System.out.println(textReport.getContent());

        Report htmlReport = generator.generate("html", "Sales Q1", "Some data...");
        System.out.println(htmlReport.getContent());
    }
}
```

---

## 🎯 Завдання

Перепроєктуй цей код, використовуючи **один зі створювальних патернів GoF** (той, що я заздалегідь обрав).
Твоя реалізація має:

* Позбутися `if/else` у `ReportGenerator`.
* Дати можливість легко додавати нові формати звітів.
* Зробити процес створення звіту більш гнучким і розширюваним.

---

Хочеш одразу взятися за реалізацію?
