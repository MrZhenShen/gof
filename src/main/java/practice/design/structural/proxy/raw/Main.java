package practice.design.structural.proxy.raw;

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
