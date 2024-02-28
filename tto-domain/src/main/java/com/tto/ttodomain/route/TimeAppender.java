package com.tto.ttodomain.route;


import com.tto.ttodomain.transportation.FirstUseTransportationDto;
import com.tto.ttodomain.transportation.Transportation;
import com.tto.ttodomain.transportation.bus.Bus;
import com.tto.ttodomain.transportation.bus.BusArrivalTimeAppender;
import com.tto.ttodomain.transportation.subway.Subway;
import com.tto.ttodomain.transportation.subway.SubwayArrivalTimeAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TimeAppender {

    private final BusArrivalTimeAppender busArrivalTimeAppender;
    private final SubwayArrivalTimeAppender subwayArrivalTimeAppender;


    public List<TimeRoute> execute(List<Route> routes) {
        List<TimeRoute> timeRoutes = new ArrayList<>();

        for (Route route : routes) {
            FirstUseTransportationDto dto = FirstUseTransportationDto.from(route.transportations());
            Transportation transportation = dto.transportation();
            int walkTime = dto.walkTime();

            if (transportation instanceof Bus bus) {
                for (LocalDateTime timeToOut : busArrivalTimeAppender.execute(
                        bus.getStartLocalStationID(),
                        bus.getRouteId(),
                        walkTime
                )) {
                    timeRoutes.add(new TimeRoute(timeToOut, route));
                }
            }
            else if (transportation instanceof Subway subway) {
                for (LocalDateTime timeToOut : subwayArrivalTimeAppender.execute(
                        subway.getStartID(),
                        subway.getWayCode(),
                        walkTime
                )) {
                    timeRoutes.add(new TimeRoute(timeToOut, route));
                }
            }
        }

        return timeRoutes;
    }
}
