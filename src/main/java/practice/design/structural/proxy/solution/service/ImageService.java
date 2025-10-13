package practice.design.structural.proxy.solution.service;

import practice.design.structural.proxy.solution.model.ImageData;

public interface ImageService {
    ImageData fetch(String imageId, String token);
}
