package practice.design.structural.facade.solution.service;

import practice.design.structural.facade.solution.model.NormalizedAudio;
import practice.design.structural.facade.solution.model.DecodedVideo;

public class AudioNormalizer {
    NormalizedAudio normalize(DecodedVideo v) {
        System.out.println("[audio] normalize");
        return new NormalizedAudio("audio.tmp");
    }
}
