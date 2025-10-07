package practice.design.structural.facade.solution.service;

import java.util.List;

import practice.design.structural.facade.solution.model.DecodedVideo;
import practice.design.structural.facade.solution.model.Thumbnail;

public class ThumbnailGenerator {
    List<Thumbnail> generate(DecodedVideo v) {
        System.out.println("[thumb] generate");
        return List.of(new Thumbnail("t1.jpg"), new Thumbnail("t2.jpg"));
    }
}
