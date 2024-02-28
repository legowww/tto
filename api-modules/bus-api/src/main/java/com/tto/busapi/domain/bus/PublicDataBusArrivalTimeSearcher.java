package com.tto.busapi.domain.bus;

import com.tto.busapi.config.PublicDataProperties;
import com.tto.ttodomain.transportation.bus.BusArrivalTimeResponse;
import com.tto.ttodomain.transportation.bus.BusArrivalTimeSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PublicDataBusArrivalTimeSearcher implements BusArrivalTimeSearcher {

    private final PublicDataProperties properties;
    private final PublicDataApi publicDataApi;
    private final PublicDataBusResponseMapper publicDataBusResponseMapper;

    @Override
    public List<LocalDateTime> execute(String bstopId, String routeId) {
        List<LocalDateTime> times = new ArrayList<>();

        LocalDateTime currTime = LocalDateTime.now();
        String currBstopId = bstopId;

        for (int i = 0; i < 3; ++i) {
            String apiResponse = publicDataApi.callBusArrivalListApi(
                    properties.getBus().getKey(),
                    properties.getBus().getPageNo(),
                    properties.getBus().getNumOfRows(),
                    bstopId,
                    routeId
            );

            Optional<BusArrivalTimeResponse> opt = publicDataBusResponseMapper.execute(apiResponse);

            if (opt.isPresent()) {
                BusArrivalTimeResponse response = opt.get();
                LocalDateTime busArrivalTime = calculateBusArrivalTime(currTime, response.arrivalEstimateTime());
                currBstopId = response.latestStopId();
                currTime = busArrivalTime;
                times.add(busArrivalTime);
            }
        }

        return times;
    }

    private LocalDateTime calculateBusArrivalTime(LocalDateTime currTime, int arrivalEstimateTime) {
        return currTime.plusSeconds(arrivalEstimateTime);
    }
}
