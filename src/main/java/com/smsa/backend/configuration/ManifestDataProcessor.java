package com.smsa.backend.configuration;

import com.smsa.backend.model.ManifestData;
import org.springframework.batch.item.ItemProcessor;

public class ManifestDataProcessor implements ItemProcessor<ManifestData, ManifestData> {
    @Override
    public ManifestData process(ManifestData manifestData) throws Exception {
        return manifestData;
    }
}
