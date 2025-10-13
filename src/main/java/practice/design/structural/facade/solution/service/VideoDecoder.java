package practice.design.structural.facade.solution.service;

import practice.design.structural.facade.solution.model.DecodedVideo;
import practice.design.structural.facade.solution.model.RawVideo;

public class VideoDecoder {
    DecodedVideo decode(RawVideo v) {
        System.out.println("[decode] " + v.path);
        return new DecodedVideo("decoded.tmp");
    }
}
