package com.tto.ttodomain.route;


import com.tto.ttodomain.location.LocationCoordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RouteService {

    private final RouteSearcher routeSearcher;
    private final TimeAppender timeAppender;
    private final TimeRouteSelector timeRouteSelector;


    public RouteFindResponse findRoutes(LocationCoordinate locationCoordinate) {
        List<Route> routes = routeSearcher.execute(locationCoordinate); //경로(출발지->목적지)
        List<TimeRoute> timeRoutes = timeAppender.execute(routes); //경로+나갈시간
        List<TimeRoute> selectedTimeRoutes = timeRouteSelector.execute(timeRoutes); //최단시간 경로 추출

        return new RouteFindResponse(selectedTimeRoutes);
    }
}
