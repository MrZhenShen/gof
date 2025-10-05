package practice.design.structural.proxy.solution.service;

import practice.design.structural.proxy.solution.model.ImageData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CachedImageService implements ImageService {
    private final ImageService imageService;
    private final ConcurrentMap<String, ImageData> cache;

    public CachedImageService() {
        imageService = new RemoteImageService();
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public ImageData fetch(String imageId, String token) {
        if (isInvalidToken(token)) return null;

        return getImage(imageId, token);
    }

    private ImageData getImage(String imageId, String token) {
        ImageData image = cache.get(imageId);
        if (image != null) {
            System.out.println("[CACHE] hit " + imageId);
            return image;
        }

        image = imageService.fetch(imageId, token);
        cache.put(image.getId(), image);

        return image;
    }

    private boolean isInvalidToken(String token) {
        return token == null;
    }
}
