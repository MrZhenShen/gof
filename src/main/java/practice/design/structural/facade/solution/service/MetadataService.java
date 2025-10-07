package practice.design.structural.facade.solution.service;

import java.util.List;

import practice.design.structural.facade.solution.model.Thumbnail;

public class MetadataService {
    void publish(String id, String title, String url, List<Thumbnail> thumbs) {
        System.out.println("[meta] publish " + id + " " + title + " " + url + " thumbs=" + thumbs.size());
    }
}
