package practice.design.structural.proxy.solution.model;

import java.time.Instant;

public class ImageData {
    private final String id;
    private final byte[] bytes;
    private final Instant loadedAt = Instant.now();

    public ImageData(String id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "ImageData{id=%s, size=%d, at=%s}".formatted(id, bytes.length, loadedAt);
    }

    public String getId() {
        return id;
    }
}
