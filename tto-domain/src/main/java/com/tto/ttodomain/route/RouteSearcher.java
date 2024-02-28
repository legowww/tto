package com.tto.ttodomain.route;

import com.tto.ttodomain.location.LocationCoordinate;

import java.util.List;

public interface RouteSearcher {

    List<Route> execute(LocationCoordinate locationCoordinate);
}
