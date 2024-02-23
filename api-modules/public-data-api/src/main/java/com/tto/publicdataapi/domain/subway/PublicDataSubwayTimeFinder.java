package com.tto.publicdataapi.domain.subway;

import com.tto.publicdataapi.config.PublicDataProperties;
import com.tto.ttodomain.transportation.SubwayTimeFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PublicDataSubwayTimeFinder implements SubwayTimeFinder {

    private final PublicDataProperties publicDataProperties;
    @Override
    public void execute(String stationId, String wayCode) {

    }
}
