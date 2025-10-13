## 📌 Проблема

Є сервіс завантаження зображень із віддаленого CDN.
Клієнтський код напряму ходить у віддалений сервіс, через що:

* кожен повторний запит того ж зображення знову йде в мережу (повільно/дорого),
* треба іноді **обмежувати частоту** запитів (rate limit),
* доступ має бути лише з **валідним токеном**,
* хотілося б **ледачо** звертатися у віддалений сервіс (тільки коли справді потрібно).

Хочемо прозоро “поставити перед” реальним сервісом проміжний шар, не змінюючи публічний інтерфейс для клієнта.

---

## 📌 Початковий код (без патерну)

```java
import java.time.Instant;
import java.util.List;
import java.util.Random;

class ImageData {
    final String id;
    final byte[] bytes;
    final Instant loadedAt = Instant.now();
    ImageData(String id, byte[] bytes) { this.id = id; this.bytes = bytes; }
    @Override public String toString() {
        return "ImageData{id=%s, size=%d, at=%s}".formatted(id, bytes.length, loadedAt);
    }
}

interface ImageService {
    ImageData fetch(String imageId, String token);
}

class RemoteImageService implements ImageService {
    private final Random rnd = new Random();

    @Override
    public ImageData fetch(String imageId, String token) {
        // Імітація мережевої затримки
        try { Thread.sleep(120); } catch (InterruptedException ignored) {}
        // Імітація байтів
        byte[] payload = new byte[1024 + rnd.nextInt(2048)];
        rnd.nextBytes(payload);
        System.out.println("[REMOTE] fetched " + imageId);
        return new ImageData(imageId, payload);
    }
}

class Gallery {
    private final ImageService service;
    private final String token;
    Gallery(ImageService service, String token) {
        this.service = service; this.token = token;
    }
    void render(List<String> ids) {
        for (String id : ids) {
            ImageData img = service.fetch(id, token); // ❌ кожен раз звертаємось у мережу
            System.out.println("Render " + img);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ImageService svc = new RemoteImageService();
        Gallery g = new Gallery(svc, "user-token-123");
        g.render(List.of("hero", "logo", "hero", "avatar", "logo"));
    }
}
```

---

## 🎯 Завдання

Перепроєктуй, застосувавши **один зі структурних патернів GoF** (той, що я обрав). Вимоги:

1. Створи прозорий “передній” шар, який має **той самий інтерфейс** `ImageService`.
2. Додай у цей шар:

    * **Кешування**: повторні `fetch(imageId, token)` повертають з кеша без звернення до `RemoteImageService`.
    * **Rate limit**, наприклад: не більш ніж `N` викликів за `M` мс (для простоти — глобально). Якщо ліміт перевищено — виведи повідомлення/кинь виняток.
    * **Перевірка токена**: якщо `token == null` або невалідний — заборонити доступ.
3. Клієнтський код (`Gallery`) не повинен змінюватися (окрім підстановки нового “сервісу” замість прямого `RemoteImageService`).
4. Опційно:

    * Ледаче очищення кеша по TTL.
    * Логування звернень, окремо позначай `[CACHE] hit` та `[REMOTE] fetched`.

---

## ✔️ Очікуваний напрямок

* `RemoteImageService` лишається як “реальна” реалізація.
* Ти створюєш “передній” компонент із тим самим інтерфейсом (`ImageService`), який делегує у `RemoteImageService`, але додає кеш/ліміт/перевірки.
* Покажи в `Main` ефект: список з повторними id → перші — `[REMOTE]`, наступні — `[CACHE]`.
