package practice.design.structural.proxy.solution;

import practice.design.structural.proxy.solution.client.Gallery;
import practice.design.structural.proxy.solution.service.CachedImageService;
import practice.design.structural.proxy.solution.service.ImageService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ImageService svc = new CachedImageService();
        Gallery g = new Gallery(svc, "user-token-123");
        g.render(List.of("hero", "logo", "hero", "avatar", "logo"));
    }
}
