package com.tto.ttodomain.transportation.bus;

import com.tto.ttodomain.transportation.Transportation;
import com.tto.ttodomain.transportation.TransportationType;
import lombok.Getter;

@Getter
public class Bus extends Transportation {

    private final String routeId;
    private final String busNum;
    private final String startLocalStationID;
    private final String startName;
    private final String endLocalStationID;
    private final String endName;

    public Bus(int time, String routeId, String busNum, String startLocalStationID, String startName, String endLocalStationID, String endName) {
        super(TransportationType.BUS, time);
        this.routeId = routeId;
        this.busNum = busNum;
        this.startLocalStationID = startLocalStationID;
        this.startName = startName;
        this.endLocalStationID = endLocalStationID;
        this.endName = endName;
    }

    @Override
    public String toString() {
        return busNum + "(" + routeId + ")" + "번 버스로 " + super.getTime() + "분 소요하여 " + startName + "(" + startLocalStationID + ")정류장 에서 " + endName + "(" + endLocalStationID + ")정류장 까지 이동";
    }
}
