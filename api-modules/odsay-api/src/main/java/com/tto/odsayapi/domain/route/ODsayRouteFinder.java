package com.tto.odsayapi.domain.route;

import com.tto.ttodomain.route.LocationCoordinate;
import com.tto.ttodomain.route.Route;
import com.tto.ttodomain.route.RouteFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ODsayRouteFinder implements RouteFinder {

    private static final String KEY = "V4gjcJsuY67y+PFRcoRcw5SQ+eo52oWIwArFQAo/XrY";
    private final ODsayApiClient ODsayApiClient;
    private final ODsayRouteMapper oDsayRouteMapper;

    @Override
    public List<Route> execute(LocationCoordinate locationCoordinate) {
        String response = ODsayApiClient.find(KEY, locationCoordinate.sx(), locationCoordinate.sy(), locationCoordinate.ex(), locationCoordinate.ey());
        List<Route> routes = oDsayRouteMapper.execute(response);
        Collections.sort(routes, Comparator.comparingInt(Route::totalTime));

        return routes;
    }
}
