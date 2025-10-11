🔥 Це саме **Chain of Responsibility** — ти зібрав ланцюжок схвалення і делегування далі. Гарна основа ✅

Однак є кілька логічних і технічних моментів, які варто виправити, плюс додати гварди за умовами з задачі.

## Що ок ✅

* Є базовий абстрактний `Approval` із делегуванням.
* Ланцюжок TL → Manager → Director → CFO побудований.
* `TeamLeadApproval` правильно **не** схвалює TRAVEL.

## Що треба виправити/додати

### 1) Умова директора (помилка з `|`)

У `DirectorApproval`:

```java
request.category() == Category.HARDWARE
        && request.amountCents() > 200_000 | request.amountCents() <= 2_000_000
```

* Використано побітовий `|` замість логічного `||`.
* Пріоритет операцій робить вираз неоднозначним.

✅ Має бути: **(HARDWARE > $2k) OR (amount ≤ $20k)**:

```java
boolean ok = (request.category() == Category.HARDWARE && request.amountCents() > 200_000)
          || (request.amountCents() <= 2_000_000);
```

### 2) Manager має не схвалювати HARDWARE > $2k

Інакше заявка на ноутбуки $4.5k пройде менеджера, а за вимогою **має** піти до директора.

```java
boolean ok = request.amountCents() <= 500_000
          && !(request.category() == Category.HARDWARE && request.amountCents() > 200_000);
```

### 3) Guard-ланка: BudgetCap (нове правило, без змін існуючих класів)

З умови: якщо `note` містить `teamA` і сума > $100k — **reject**. (У вас у центрах: $100k = `10_000_000`).

```java
public class BudgetCapGuard extends Approval {
    public BudgetCapGuard(Approval next) { super(next); }
    @Override ApprovalResult handle(ExpenseRequest r) {
        boolean overCap = r.note() != null && r.note().toLowerCase().contains("teama")
                       && r.amountCents() > 10_000_000;
        return overCap
            ? new ApprovalResult("BudgetGuard", false, "Team A quarterly cap exceeded")
            : delegate(r);
    }
}
```

### 4) Безпечне делегування в базі

`CfoApproval` викликається останнім. Щоб уникнути потенційного NPE, зроби делегування захищеним:

```java
ApprovalResult delegate(ExpenseRequest r) {
    return (higherApproval != null) ? higherApproval.handle(r)
                                    : new ApprovalResult("CFO", true, "OK > $20k");
}
```

і тоді `CfoApproval` може бути простим “термінатором”:

```java
public class CfoApproval extends Approval {
    public CfoApproval() { super(null); }
    @Override public ApprovalResult handle(ExpenseRequest r) {
        return new ApprovalResult("CFO", true, "OK > $20k");
    }
}
```

### 5) Будівник ланцюга (замість створення TL напряму в сервісі)

Так легше додавати/переставляти правила.

```java
public final class Approvals {
    public static Approval pipeline() {
        return new BudgetCapGuard( // нове правило зверху
               new TeamLeadApproval(
               new ManagerApproval(
               new DirectorApproval(
               new CfoApproval()))));
    }
}
```

Перепиши `ExpenseService`:

```java
public class ExpenseService {
    private final Approval chain = Approvals.pipeline();
    public ApprovalResult approve(ExpenseRequest r) { return chain.handle(r); }
}
```

## Патчі по класах (мінімальні зміни)

### `ManagerApproval`

```java
@Override
public ApprovalResult handle(ExpenseRequest request) {
    boolean ok = request.amountCents() <= 500_000
              && !(request.category() == Category.HARDWARE && request.amountCents() > 200_000);
    return handlePreconditionOrDelegate(request, ok,
        new ApprovalResult("Manager", true, "OK up to $5k"));
}
```

### `DirectorApproval`

```java
@Override
ApprovalResult handle(ExpenseRequest request) {
    boolean ok = (request.category() == Category.HARDWARE && request.amountCents() > 200_000)
              || (request.amountCents() <= 2_000_000);
    return handlePreconditionOrDelegate(request, ok,
        new ApprovalResult("Director", true, "OK up to $20k or HW>2k"));
}
```

### `Approval` (безпечне делегування)

```java
ApprovalResult delegate(ExpenseRequest r) {
    return (higherApproval != null) ? higherApproval.handle(r)
                                    : new ApprovalResult("CFO", true, "OK > $20k");
}
```

### Новий `BudgetCapGuard` (див. вище) + `Approvals` builder

## Швидкий прогін кейсів

* `e-1` ($350, GENERAL) → **TeamLead** OK.
* `e-2` ($1,200, TRAVEL) → **Manager** OK (TL не бере travel).
* `e-3` ($4,500, HARDWARE) → **Director** OK (бо HW > $2k).
* `e-4` ($25,000, GENERAL) → **CFO** OK (> $20k).
* `teamA` + $120,000 → **BudgetGuard** reject.

---

✅ Висновок: патерн застосовано правильно. Після правок умови відповідають бізнес-правилам, ланцюг легко розширюється (через нові guard-ланки), а сервіс не містить `if-else` з доменною логікою.
