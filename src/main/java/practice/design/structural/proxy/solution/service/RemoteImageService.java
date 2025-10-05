package practice.design.structural.proxy.solution.service;

import practice.design.structural.proxy.solution.model.ImageData;

import java.util.Random;

public class RemoteImageService implements ImageService {
    private final Random rnd = new Random();

    @Override
    public ImageData fetch(String imageId, String token) {
        try {
            Thread.sleep(120);
        } catch (InterruptedException ignored) {
        }
        byte[] payload = new byte[1024 + rnd.nextInt(2048)];
        rnd.nextBytes(payload);
        System.out.println("[REMOTE] fetched " + imageId);
        return new ImageData(imageId, payload);
    }
}
