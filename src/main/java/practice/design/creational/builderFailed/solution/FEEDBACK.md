Чудова робота! Ти прибрав `if/else` і виніс створення у окремі класи `*ReportFactory` — це акуратна **Factory Method**-реалізація ✅

Але прихований патерн на це завдання був **Builder**.

### Що в тебе добре

* Немає розгалужень у клієнті — класно розвантажили `Main`.
* Легка розширюваність: додати новий формат = додати нову фабрику.
* Лаконічний fluent-ланцюжок у `Report` (addHeader/body/footer).

### Де розбіг з очікуваним (Builder)

Ключова ідея **Builder** — **відділити процес побудови від представлення**:

* Є **Director**, який знає *послідовність кроків* (header → body → footer), але не знає деталей форматування.
* Є **Builder**-и (Text/Html/Pdf), які реалізують *як* будувати кожну частину.
* Продукт (`Report`) бажано зробити **іммутабельним** і не давати клієнту сеттери/мутатори.

Твоя версія радше фабрики різних “готових” репортів. Вона працює, але втрачає переваги Builder’а: повторне використання одного й того ж процесу з різними представленнями, можливість варіювати/пропускати кроки, іменовані етапи з хукамі тощо.

---

## Як переробити на Builder (стисла еталонна схема)

```java
// Продукт — іммутабельний
final class Report {
    private final String header, body, footer;
    public Report(String header, String body, String footer) {
        this.header = header; this.body = body; this.footer = footer;
    }
    public String getContent() { return header + "\n" + body + "\n" + footer; }
}

// Будівельник
interface ReportBuilder {
    void reset();
    void buildHeader(String title);
    void buildBody(String content);
    void buildFooter();
    Report getResult();
}

// Конкретні будівельники
class TextReportBuilder implements ReportBuilder {
    private String header, body, footer;

    public void reset() { header = body = footer = ""; }
    public void buildHeader(String title) { header = "=== " + title + " ==="; }
    public void buildBody(String content) { body = content; }
    public void buildFooter() { footer = "--- end of report ---"; }

    public Report getResult() { return new Report(header, body, footer); }
}

class HtmlReportBuilder implements ReportBuilder {
    private String header, body, footer;

    public void reset() { header = body = footer = ""; }
    public void buildHeader(String title) { header = "<h1>" + title + "</h1>"; }
    public void buildBody(String content) { body = "<p>" + content + "</p>"; }
    public void buildFooter() { footer = "<hr><i>End of report</i>"; }

    public Report getResult() { return new Report(header, body, footer); }
}

class PdfReportBuilder implements ReportBuilder {
    private String header, body, footer;

    public void reset() { header = body = footer = ""; }
    public void buildHeader(String title) { header = "[PDF HEADER: " + title + "]"; }
    public void buildBody(String content) { body = "[PDF BODY: " + content + "]"; }
    public void buildFooter() { footer = "[PDF FOOTER]"; }

    public Report getResult() { return new Report(header, body, footer); }
}

// Директор знає послідовність кроків
class ReportDirector {
    private ReportBuilder builder;
    public ReportDirector(ReportBuilder builder) { this.builder = builder; }
    public void setBuilder(ReportBuilder builder) { this.builder = builder; }

    public Report makeStandard(String title, String content) {
        builder.reset();
        builder.buildHeader(title);
        builder.buildBody(content);
        builder.buildFooter();
        return builder.getResult();
    }
}

// Використання
public class Main {
    public static void main(String[] args) {
        ReportDirector director = new ReportDirector(new TextReportBuilder());
        Report text = director.makeStandard("Sales Q1", "Some data...");
        System.out.println(text.getContent());

        director.setBuilder(new HtmlReportBuilder());
        Report html = director.makeStandard("Sales Q1", "Some data...");
        System.out.println(html.getContent());
    }
}
```

### Переваги саме такого підходу

* **Одна послідовність — різні представлення** (міняємо лише Builder).
* **Гнучкість процесу:** легко додати інші варіанти збірки (наприклад, без футера).
* **Чистіший продукт:** `Report` не має публічних мутаторів — все збирається через Builder.

---

## Якщо хочеш лишити фабрики

Можеш поєднати: фабрика повертає конкретний `ReportBuilder`, а клієнт працює через `ReportDirector`. Це зручно, якщо формат приходить як дані:

```java
interface ReportBuilderFactory { ReportBuilder create(); }
```
