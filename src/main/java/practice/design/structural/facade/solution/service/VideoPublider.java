package practice.design.structural.facade.solution.service;

import java.time.Instant;
import java.util.List;

import practice.design.structural.facade.solution.model.CdnLocation;
import practice.design.structural.facade.solution.model.DecodedVideo;
import practice.design.structural.facade.solution.model.EncodedAsset;
import practice.design.structural.facade.solution.model.PublishResult;
import practice.design.structural.facade.solution.model.RawVideo;
import practice.design.structural.facade.solution.model.Thumbnail;

public class VideoPublider {

    private final VideoDecoder decoder;
    private final AudioNormalizer audio;
    private final Encoder encoder;
    private final SubtitleMerger subtitleMerger;
    private final ThumbnailGenerator thumbs;
    private final CdnClient cdnClient;
    private final Audit audit;

    public VideoPublider() {
        decoder = new VideoDecoder();
        audio = new AudioNormalizer();
        encoder = new Encoder();
        subtitleMerger = new SubtitleMerger();
        thumbs = new ThumbnailGenerator();
        cdnClient = new CdnClient();
        audit = new Audit();
    }

    public PublishResult publish(String sourcePath) {

        String videoId = "vid-42";
        audit.log("start " + videoId);

        DecodedVideo decodedVideo = decoder.decode(new RawVideo(sourcePath));

        EncodedAsset asset = encoder.encode(
                subtitleMerger.merge(
                        audio.normalize(decodedVideo),
                        "input/subs.srt"),
                "h264_1080p");

        List<Thumbnail> thumbnails = thumbs.generate(decodedVideo);

        CdnLocation cdnLocation = cdnClient.upload(asset);

        var meta = new MetadataService();
        meta.publish(videoId, "Travel Vlog", cdnLocation.url, thumbnails);

        var billing = new Billing();
        billing.charge("acct-007", 199);

        audit.log("done " + videoId + " -> " + cdnLocation.url);

        return new PublishResult(videoId, cdnLocation.url, Instant.now());
    }
}
