package com.tto.routeapi.domain.route;

import com.tto.routeapi.config.ODsayProperties;
import com.tto.ttodomain.location.LocationCoordinate;
import com.tto.ttodomain.route.Route;
import com.tto.ttodomain.route.RouteSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ODsayRouteApiClient implements RouteSearcher {

    private final ODsayProperties properties;
    private final ODsayRouteSearcher oDsayRouteApi;
    private final ODsayRouteResponseMapper oDsayRouteResponseMapper;

    @Override
    public List<Route> execute(LocationCoordinate locationCoordinate) {
        String apiResponse = oDsayRouteApi.find(
                properties.getKey(),
                locationCoordinate.sx(),
                locationCoordinate.sy(),
                locationCoordinate.ex(),
                locationCoordinate.ey()
        );
        List<Route> routes = oDsayRouteResponseMapper.execute(apiResponse);

        return routes;
    }
}
