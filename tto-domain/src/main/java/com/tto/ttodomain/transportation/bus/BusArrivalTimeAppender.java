package com.tto.ttodomain.transportation.bus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BusArrivalTimeAppender {

    private static final int BUS_CORRECTION_TIME_MINUTE = 2;
    private final BusArrivalTimeSearcher busArrivalTimeSearcher;

    public List<LocalDateTime> execute(String startLocalStationID, String routeId, int walkTime) {
        //외부API 영역: 추상화된 응답값(List<LocalDateTime>)
        List<LocalDateTime> busArrivalTimes = busArrivalTimeSearcher.execute(startLocalStationID, routeId);

        //도메인 영역: 보정시간 더해주기
        return busArrivalTimes
                .stream()
                .map(busArrivalTime -> BusArrivalTimeAppender.calculateOutTime(walkTime, busArrivalTime))
                .toList();
    }

    /*
    나갈시간 = 버스도착시간- (정류장까지걷는시간 + 보정시간)
     */
    private static LocalDateTime calculateOutTime(int walkTime, LocalDateTime busArrivalTime) {
        return busArrivalTime.minusMinutes(walkTime + BUS_CORRECTION_TIME_MINUTE);
    }
}
