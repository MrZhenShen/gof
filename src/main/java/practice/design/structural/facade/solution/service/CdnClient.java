package practice.design.structural.facade.solution.service;

import practice.design.structural.facade.solution.model.CdnLocation;
import practice.design.structural.facade.solution.model.EncodedAsset;

public class CdnClient {
    CdnLocation upload(EncodedAsset a) {
        System.out.println("[cdn] upload " + a.file);
        return new CdnLocation("https://cdn.example/" + a.file);
    }
}
