package com.tto.ttodomain.transportation.subway;

import com.tto.ttodomain.transportation.Transportation;
import com.tto.ttodomain.transportation.TransportationType;
import lombok.Getter;

@Getter
public class Subway extends Transportation {

    private final String startID;
    private final String wayCode;
    private final String startName;
    private final String endName;

    public Subway(int time, String startID, String wayCode, String startName, String endName) {
        super(TransportationType.SUBWAY, time);
        this.startID = startID;
        this.wayCode = wayCode;
        this.startName = startName;
        this.endName = endName;
    }

    @Override
    public String toString() {
        return wayCode + "행 " +
                startName + "(" +
                startID + ")역 에서 " +
                super.getTime() + "분 소요하여 " +
                endName + "역 까지 이동";
    }
}
