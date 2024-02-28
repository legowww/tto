package com.tto.ttodomain.route;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
public class TimeRouteSelector {

    private final static int LIMIT_COUNT = 5;

    public List<TimeRoute> execute(List<TimeRoute> timeRoutes) {
        LocalDateTime now = LocalDateTime.now();

        return timeRoutes.stream()
                .filter(timeRoute -> timeRoute.timeToOut().compareTo(now) != -1)
                .limit(LIMIT_COUNT)
                .sorted(Comparator.comparing(TimeRoute::timeToOut))
                .toList();
    }
}
