## 📌 Проблема

Уяви, що ти розробляєш систему для роботи з **різними типами документів**: `Word`, `PDF`, `Excel`.
Зараз у коді є клас `DocumentManager`, який напряму створює об’єкти документів.

Проблеми з таким підходом:

* Якщо додати новий тип документа — доведеться змінювати `DocumentManager`.
* Код створення розкиданий у різних місцях.
* Важко підтримувати і розширювати.

---

## 📌 Початковий код (без патерну)

```java
// Базовий інтерфейс для документів
interface Document {
    void open();
}

// Конкретні реалізації
class WordDocument implements Document {
    public void open() {
        System.out.println("Opening Word document...");
    }
}

class PdfDocument implements Document {
    public void open() {
        System.out.println("Opening PDF document...");
    }
}

class ExcelDocument implements Document {
    public void open() {
        System.out.println("Opening Excel document...");
    }
}

// Менеджер документів
class DocumentManager {
    public Document createDocument(String type) {
        if (type.equalsIgnoreCase("word")) {
            return new WordDocument();
        } else if (type.equalsIgnoreCase("pdf")) {
            return new PdfDocument();
        } else if (type.equalsIgnoreCase("excel")) {
            return new ExcelDocument();
        }
        throw new IllegalArgumentException("Unknown document type: " + type);
    }
}

// Демонстрація
public class Main {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        Document doc1 = manager.createDocument("word");
        doc1.open();

        Document doc2 = manager.createDocument("pdf");
        doc2.open();
    }
}
```

---

Твоє завдання 🎯
Перепроєктуй цей код, застосувавши **один із створювальних патернів GoF**, який я випадково обрав.

