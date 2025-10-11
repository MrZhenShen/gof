## 📌 Проблема

У компанії діє політика погодження витрат. Заявка (`ExpenseRequest`) має суму, категорію та опис.
Зараз у сервісі стоїть **велике if-else**: до 500$ — TeamLead, до 5,000$ — Manager, до 20,000$ — Director, інакше — CFO.
Додаткові умови — “travel” завжди вимагає принаймні Manager; “hardware” понад 2,000$ вимагає Director. Код розростається, правила важко змінювати й тестувати, а локальні перевірки дублюються.

Хочемо зробити **гнучкий конвеєр погодження**, де окремі ланки вирішують: **схвалити**, **відхилити** або **передати далі**.

---

## 📌 Початковий код (без патерну)

```java
import java.time.Instant;

enum Category { GENERAL, TRAVEL, HARDWARE }

record ExpenseRequest(String id, long amountCents, Category category, String note, Instant createdAt) {
    ExpenseRequest(String id, long amountCents, Category category, String note) {
        this(id, amountCents, category, note, Instant.now());
    }
}

class ApprovalResult {
    final String approver;
    final boolean approved;
    final String message;
    ApprovalResult(String approver, boolean approved, String message) {
        this.approver = approver; this.approved = approved; this.message = message;
    }
    @Override public String toString() {
        return "Approval{by=%s, ok=%s, msg=%s}".formatted(approver, approved, message);
    }
}

class ExpenseService {
    ApprovalResult approve(ExpenseRequest r) {
        // ❌ велика розгалужена логіка
        if (r.amountCents() <= 50_000) { // <= $500
            if (r.category() == Category.TRAVEL) {
                // travel не можна TL — передаємо вище
            } else {
                return new ApprovalResult("TeamLead", true, "OK up to $500");
            }
        }
        if (r.amountCents() <= 500_000) { // <= $5,000
            return new ApprovalResult("Manager", true, "OK up to $5k");
        }
        if (r.category() == Category.HARDWARE && r.amountCents() > 200_000) { // > $2,000
            // хочемо Director мінімум
        }
        if (r.amountCents() <= 2_000_000) { // <= $20,000
            return new ApprovalResult("Director", true, "OK up to $20k");
        }
        // все інше — CFO
        return new ApprovalResult("CFO", true, "OK > $20k");
    }
}

public class Main {
    public static void main(String[] args) {
        var svc = new ExpenseService();
        System.out.println(svc.approve(new ExpenseRequest("e-1", 35_000, Category.GENERAL, "Team offsite snacks"))); // ~ $350
        System.out.println(svc.approve(new ExpenseRequest("e-2", 120_000, Category.TRAVEL, "Flights")));              // $1,200
        System.out.println(svc.approve(new ExpenseRequest("e-3", 450_000, Category.HARDWARE, "New laptops")));       // $4,500
        System.out.println(svc.approve(new ExpenseRequest("e-4", 2_500_000, Category.GENERAL, "Booth")));            // $25,000
    }
}
```

---

## 🎯 Завдання

Перепроєктуй це, застосувавши **один із поведінкових патернів GoF** (той, що я обрав).

Вимоги до твого рішення:

1. Введи абстракцію ланки конвеєра з методом на кшталт `ApprovalResult handle(ExpenseRequest r)`, і можливістю **передати далі**.
2. Зроби окремі ланки для бізнес-правил:

   * `TeamLeadApproval` (до $500, але **не** для TRAVEL),
   * `ManagerApproval` (до $5k або будь-який TRAVEL до $5k),
   * `DirectorApproval` (до $20k **або** HARDWARE понад $2k),
   * `CfoApproval` (все інше — фінальний стоп).
     Дозволяється мати “перевірочні” ланки типу `HardwareEscalation` або `TravelEscalation`, які лише **ескалюють/відхиляють/переадресовують**.
3. Побудуй конвеєр у `Main` (або в окремому `Approvals` builder’і) і проганяй кілька кейсів.
4. Додай ще одне правило **без зміни існуючих класів** (наприклад, `BudgetCapGuard`, що відхиляє все понад $100k у квартал для Team A — можеш спростити до “якщо note містить `teamA` і сума > $100k — reject”).

> Ключ: кожна ланка **або** вирішує, **або** делегує наступній — без if-else у центральному сервісі.
