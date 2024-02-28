package com.tto.routeapi.domain.route;

import com.tto.ttodomain.route.Route;
import com.tto.ttodomain.transportation.*;
import com.tto.ttodomain.transportation.bus.Bus;
import com.tto.ttodomain.transportation.subway.Subway;
import com.tto.ttodomain.transportation.walk.Walk;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class ODsayRouteResponseMapper {

    public List<Route> execute(String response) {
        JSONObject json = new JSONObject(response);
        JSONObject result = (JSONObject) json.get("result");
        JSONArray paths = (JSONArray) result.get("path");

        List<Route> routes = new ArrayList<>();
        for (int i = 0; i < paths.length(); ++i) {
            JSONObject path = (JSONObject) paths.get(i);
            JSONObject info = (JSONObject) path.get("info");

            String firstStartStation = info.get("firstStartStation").toString();
            String lastEndStation = info.get("lastEndStation").toString();
            String busTransitCount = info.get("busTransitCount").toString();
            String subwayTransitCount = info.get("subwayTransitCount").toString();
            int totalTime = Integer.parseInt(info.get("totalTime").toString());

            List<Transportation> transportations = new ArrayList<>();
            JSONArray subPath = (JSONArray) path.get("subPath");
            for (int j = 0; j < subPath.length(); ++j) {
                JSONObject sp = (JSONObject) subPath.get(j);
                TransportationType transportationType = getTrafficType(Integer.parseInt(sp.get("trafficType").toString()));
                int sectionTime = Integer.parseInt(sp.get("sectionTime").toString());

                if (transportationType == TransportationType.WALK) {
                    if (sectionTime == 0) {
                        continue;
                    }
                    if (sectionTime == 1) {
                        totalTime -= 1;
                        continue;
                    }

                    transportations.add(new Walk(sectionTime));
                }
                else if (transportationType == TransportationType.BUS) {
                    String startName = sp.get("startName").toString();
                    String endName = sp.get("endName").toString();
                    String startLocalStationID = sp.get("startLocalStationID").toString();
                    String endLocalStationID = sp.get("endLocalStationID").toString();

                    JSONArray lane = (JSONArray) sp.get("lane");
                    JSONObject lane_bus = (JSONObject) lane.get(0);

                    String busNum = lane_bus.get("busNo").toString();
                    String busId = lane_bus.get("busLocalBlID").toString();

                    transportations.add(new Bus(sectionTime, busId, busNum, startLocalStationID, startName, endLocalStationID, endName));
                }
                else if (transportationType == TransportationType.SUBWAY) {
                    String startID = sp.get("startID").toString();
                    String wayCode = sp.get("wayCode").toString();
                    String startName = sp.get("startName").toString();
                    String endName = sp.get("endName").toString();

                    transportations.add(new Subway(sectionTime, startID, wayCode, startName, endName));
                }
            }
            routes.add(new Route(totalTime, busTransitCount, subwayTransitCount, firstStartStation, lastEndStation, transportations));
        }

        Collections.sort(routes, Comparator.comparingInt(Route::totalTime));

        return routes;
    }

    private TransportationType getTrafficType (int type) {
        return switch (type) {
            case 1 -> TransportationType.SUBWAY;
            case 2 -> TransportationType.BUS;
            default -> TransportationType.WALK;
        };
    }
}
