package practice.design.structural.proxy.solution.client;

import practice.design.structural.proxy.solution.model.ImageData;
import practice.design.structural.proxy.solution.service.ImageService;

import java.util.List;

public class Gallery {
    private final ImageService service;
    private final String token;

    public Gallery(ImageService service, String token) {
        this.service = service;
        this.token = token;
    }

    public void render(List<String> ids) {
        for (String id : ids) {
            ImageData img = service.fetch(id, token);
            System.out.println("Render " + img);
        }
    }
}
