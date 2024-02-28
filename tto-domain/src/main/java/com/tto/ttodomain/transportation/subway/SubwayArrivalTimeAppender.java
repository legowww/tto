package com.tto.ttodomain.transportation.subway;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Component
public class SubwayArrivalTimeAppender {

    private static final int SUBWAY_CORRECTION_TIME_MINUTE = 4;
    private final SubwayArrivalTimeService subwayArrivalTimeService;

    public List<LocalDateTime> execute(String startID, String wayCode, int walkTime) {
        if (checkUnableTime()) {
            return Collections.emptyList();
        }

        List<LocalDateTime> subwayArrivalTimes = subwayArrivalTimeService.execute(startID, wayCode);

        return subwayArrivalTimes
                .stream()
                .map(subwayArrivalTime -> SubwayArrivalTimeAppender.calculateOutTime(walkTime, subwayArrivalTime))
                .toList();
    }

    private boolean checkUnableTime() {
        LocalTime now = LocalTime.now();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalTime earlyMorning = LocalTime.of(5, 0);

        if ((now.isAfter(midnight) || now.equals(midnight)) && now.isBefore(earlyMorning)) {
            return true;
        } else {
            return false;
        }
    }

    /*
    나갈시간 = 버스도착시간 - (정류장까지걷는시간 + 보정시간)
     */
    private static LocalDateTime calculateOutTime(int walkTime, LocalDateTime busArrivalTime) {
        return busArrivalTime.minusMinutes(walkTime + SUBWAY_CORRECTION_TIME_MINUTE);
    }
}
