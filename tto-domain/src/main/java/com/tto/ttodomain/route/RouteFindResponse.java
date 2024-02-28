package com.tto.ttodomain.route;

import java.util.List;

public record RouteFindResponse(
    List<TimeRoute> timeRoutes
) {
}
