package practice.design.structural.facade.solution.service;

import practice.design.structural.facade.solution.model.MuxedMedia;
import practice.design.structural.facade.solution.model.NormalizedAudio;

public class SubtitleMerger {
    MuxedMedia merge(NormalizedAudio a, String subs) {
        System.out.println("[subs] merge " + subs);
        return new MuxedMedia("mux.tmp");
    }
}
