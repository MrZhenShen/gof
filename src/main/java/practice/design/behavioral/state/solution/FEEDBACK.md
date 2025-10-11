🔥 Це гарна реалізація **State**: поведінка інкапсульована в окремих станах, а `Order` делегує виклики поточному стану. Загальна структура — в точку ✅

Ось що варто підправити й як можна підсилити.

## Логіка станів — дрібні покращення

* `OrderDelivered.next()` просто друкує “Finalized”. Краще узгодити поведінку: або кидати `BusinessRuleViolation`, або залишити як є, але це виняток із правила (в інших фінальних — `Cancelled/Refunded` — ти кидаєш).

  Напр., зробити як у фінальних станах:

  ```java
  @Override public void next() { notifyUnavailableAction(); }
  ```

* У `OrderCanceled` і `OrderRefunded` ти забороняєш усі дії — це коректно для фінальних станів.

## Стилістика / читабельність

* `OrderState.toString()` використовується як статус. Це ок, але можна додати явний метод:

  ```java
  public String name() { return toString(); }
  ```

  або зробити `enum`-подібну константу всередині кожного стану.

* `changeState` логічно виглядає, гарно що логуються переходи.

## (Опційно) Гачки життєвого циклу

Іноді корисно мати `enter()`/`exit()` для станів (логування, побічні ефекти):

```java
public abstract class OrderState {
    protected final Order order;
    protected OrderState(Order order){ this.order = order; }
    public void enter() {}
    public void exit() {}
    // ...
}
```

```java
public void changeState(OrderState state) {
    if (state == null) return;
    this.state.exit();
    logTransition(this.state.toString(), state.toString());
    this.state = state;
    this.state.enter();
}
```

## (Опційно) Новий стан «On Hold»

Показує, як легко розширити:

```java
public class OrderOnHold extends OrderState {
    public OrderOnHold(Order order){ super(order); }
    @Override public void next() { notifyUnavailableAction(); }
    @Override public void cancel() { order.changeState(new OrderCanceled(order)); }
    @Override public void pay() { order.changeState(new OrderPaid(order)); } // наприклад, оплата знімає hold
    @Override public String toString() { return "On Hold"; }
}
```

## Патчі (мінімальні правки)

**Order.java**

```java
public class Order {
    private OrderState state;

    public Order() { state = new OrderNew(this); }

    public void changeState(OrderState state) {
        if (state == null) { System.err.println("Status set to undefined. Keep Status."); return; }
        logTransition(this.state.toString(), state.toString());
        this.state = state;
    }

    public void next()    { state.next(); }
    public void cancel()  { state.cancel(); }
    public void pay()     { state.pay(); }
    public void refund()  { state.refund(); }   // ← фікс
    public void ship()    { state.ship(); }
    public void deliver() { state.deliver(); }

    public String getStatus() { return state.toString(); }

    private void logTransition(String from, String to) {
        System.out.printf("Order transition: %s -> %s%n", from, to);
    }
}
```

**OrderState.java** (оновлене повідомлення без `\n`)

```java
void notifyUnavailableAction(String action) {
    throw new BusinessRuleViolation("%s is not available when %s".formatted(action, order.getStatus()));
}
```

**OrderDelivered.java** (варіант уніфікації)

```java
@Override public void next() { notifyUnavailableAction(); }
```

---

### Підсумок

* Патерн **State** реалізований правильно ✅
* Обов’язково виправ `refund()` → `state.refund()`.
* За бажанням — уніфікуй фінальні стани, додай гачки `enter/exit`, легко зможеш вводити нові стани (`OnHold`, `RefundedPartial`, тощо).
