Круто! Ти реалізував саме **Mediator**, користувачі не знають одне про одного, є broadcast/PM/system. ✅

Ось що підкрутити.

## Головні баги

1. **Порівняння рядків через `==`**
   У Java рядки порівнюємо `.equals(...)`, інакше отримаєш випадкові промахи.

* `member.getName() != message.sender()` → `!member.getName().equals(message.sender())`
* `member.getName() == message.consumer()` → `member.getName().equals(message.consumer())`

2. **Перевірка чату в `User.sendTo`**

```java
public void sendTo(String message, String recipient) {
    if (chat == null) return;              // було: if (chat == null && recipient == name)
    chat.sendTo(new Message(name, message, recipient));
}
```

## Невеликі покращення API/поведінки

* Якщо адресат приватного повідомлення **не в чаті**, краще повідомляти відправника.
* Додай `removeMember(...)`.
* Тримай індекс за іменем для O(1) пошуку.

## Патчі (мінімальні)

### `Chat`

```java
public class Chat {
    private final Set<User> members = new HashSet<>();
    private final Map<String, User> byName = new HashMap<>();
    private static final String SYSTEM_SENDER = "System";

    public void addMember(User newMember) {
        members.add(newMember);
        byName.put(newMember.getName(), newMember);
        newMember.setChat(this);
    }

    public void removeMember(User member) {
        members.remove(member);
        byName.remove(member.getName());
        member.setChat(null);
    }

    public void send(Message message) {
        logMessage(message.sender(), message.content());
        for (User member : members) {
            if (!member.getName().equals(message.sender())) {
                member.chatEventConsumer(message);
            }
        }
    }

    public void sendTo(Message message) {
        logMessage(message.sender(), "[pm->" + message.consumer() + "] " + message.content());
        User target = byName.get(message.consumer());
        if (target != null) {
            target.chatEventConsumer(message);
        } else {
            // опційно: повідомити відправника, що адресата нема
            User sender = byName.get(message.sender());
            if (sender != null) {
                sender.chatEventConsumer(new Message(
                    SYSTEM_SENDER, "User '" + message.consumer() + "' not found", message.sender()));
            }
        }
    }

    public void broadcastSystem(String messageContent) {
        logMessage(SYSTEM_SENDER, messageContent);
        Message sys = new Message(SYSTEM_SENDER, messageContent, null);
        for (User member : members) {
            member.chatEventConsumer(sys);
        }
    }

    private void logMessage(String sender, String messageContent) {
        System.out.println(sender + " sends: " + messageContent);
    }
}
```

### `User`

```java
public void sendTo(String message, String recipient) {
    if (chat == null) return;
    chat.sendTo(new Message(name, message, recipient));
}
```

## Опційні фічі (якщо захочеш)

* **Mute:** у `Chat` тримати `Map<String, Set<String>> muted`, і в `send(...)` пропускати тих, хто зам’ютив відправника.
* **Інтерфейси:** `interface Mediator { ... }`, `interface Participant { ... }` — полегшує тестування/розширення.
* **Системні події:** при `add/remove` відправляти системні повідомлення “X joined/left”.

З цими правками матимеш надійний, чистий **Mediator** без прихованих багів зі строками 💪
