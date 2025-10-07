## 📌 Проблема

Є модуль, що готує відео до публікації: декодування, нормалізація звуку, злиття субтитрів, генерація постерів, повторне кодування, завантаження в CDN, публікація метаданих, білінг та аудит.
Клієнтський код сам керує всім пайплайном і знає про деталі кожного кроку — це крихко, багато дублів і важко підтримувати.

---

## 📌 Початковий код (без патерну)

```java
import java.time.Instant;
import java.util.List;

class RawVideo { final String path; RawVideo(String p){ this.path=p; } }
class DecodedVideo { final String tmp; DecodedVideo(String t){ this.tmp=t; } }
class NormalizedAudio { final String tmp; NormalizedAudio(String t){ this.tmp=t; } }
class MuxedMedia { final String tmp; MuxedMedia(String t){ this.tmp=t; } }
class EncodedAsset { final String file; EncodedAsset(String f){ this.file=f; } }
class Thumbnail { final String file; Thumbnail(String f){ this.file=f; } }
class CdnLocation { final String url; CdnLocation(String u){ this.url=u; } }
record PublishResult(String id, String url, Instant at) {}

class VideoDecoder { DecodedVideo decode(RawVideo v){ System.out.println("[decode] "+v.path); return new DecodedVideo("decoded.tmp"); } }
class AudioNormalizer { NormalizedAudio normalize(DecodedVideo v){ System.out.println("[audio] normalize"); return new NormalizedAudio("audio.tmp"); } }
class SubtitleMerger { MuxedMedia merge(NormalizedAudio a, String subs){ System.out.println("[subs] merge "+subs); return new MuxedMedia("mux.tmp"); } }
class ThumbnailGenerator { List<Thumbnail> generate(DecodedVideo v){ System.out.println("[thumb] generate"); return List.of(new Thumbnail("t1.jpg"), new Thumbnail("t2.jpg")); } }
class Encoder { EncodedAsset encode(MuxedMedia m, String preset){ System.out.println("[encode] "+preset); return new EncodedAsset("out_"+preset+".mp4"); } }
class CdnClient { CdnLocation upload(EncodedAsset a){ System.out.println("[cdn] upload "+a.file); return new CdnLocation("https://cdn.example/"+a.file); } }
class MetadataService { void publish(String id, String title, String url, List<Thumbnail> thumbs){ System.out.println("[meta] publish "+id+" "+title+" "+url+" thumbs="+thumbs.size()); } }
class Billing { void charge(String accountId, long cents){ System.out.println("[billing] "+accountId+" +"+cents); } }
class Audit { void log(String msg){ System.out.println("[audit] "+msg); } }

public class Main {
    public static void main(String[] args) {
        // клієнт знає всі кроки і порядок
        var audit = new Audit();
        var decoder = new VideoDecoder();
        var audio = new AudioNormalizer();
        var subs = new SubtitleMerger();
        var thumbs = new ThumbnailGenerator();
        var encoder = new Encoder();
        var cdn = new CdnClient();
        var meta = new MetadataService();
        var billing = new Billing();

        String videoId = "vid-42";
        audit.log("start " + videoId);

        RawVideo raw = new RawVideo("input/source.mov");
        DecodedVideo dv = decoder.decode(raw);
        NormalizedAudio na = audio.normalize(dv);
        MuxedMedia mux = subs.merge(na, "input/subs.srt");
        List<Thumbnail> th = thumbs.generate(dv);
        EncodedAsset asset = encoder.encode(mux, "h264_1080p");
        CdnLocation loc = cdn.upload(asset);
        meta.publish(videoId, "Travel Vlog", loc.url, th);
        billing.charge("acct-007", 199); // 1.99$

        audit.log("done " + videoId + " -> " + loc.url);
        System.out.println(new PublishResult(videoId, loc.url, Instant.now()));
    }
}
```

---

## 🎯 Завдання

Перероби так, щоб **клієнтський код** працював із **єдиною точкою входу**, не знаючи внутрішніх підсистем і їхньої послідовності.
Підтримай конфігурацію пресету кодування, шляхів субтитрів, білінгу та заголовка відео через зрозумілий API цієї точки входу.
Демонстрація у `Main` має лишитися простою (1–2 виклики замість “ритуального танцю” з десятка кроків).
