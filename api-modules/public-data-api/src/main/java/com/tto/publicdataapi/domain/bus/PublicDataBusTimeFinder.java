package com.tto.publicdataapi.domain.bus;

import com.tto.publicdataapi.config.PublicDataProperties;
import com.tto.ttodomain.transportation.BusTimeFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PublicDataBusTimeFinder implements BusTimeFinder {

    private final PublicDataProperties publicDataProperties;

    @Override
    public void execute(String busStopId, String routeId) {

    }
}
