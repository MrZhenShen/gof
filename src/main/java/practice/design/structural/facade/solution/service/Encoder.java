package practice.design.structural.facade.solution.service;

import practice.design.structural.facade.solution.model.EncodedAsset;
import practice.design.structural.facade.solution.model.MuxedMedia;

public class Encoder {
    EncodedAsset encode(MuxedMedia m, String preset) {
        System.out.println("[encode] " + preset);
        return new EncodedAsset("out_" + preset + ".mp4");
    }
}
