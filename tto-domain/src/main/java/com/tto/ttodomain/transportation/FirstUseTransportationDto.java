package com.tto.ttodomain.transportation;

import com.tto.ttodomain.transportation.bus.Bus;
import com.tto.ttodomain.transportation.subway.Subway;

import java.util.List;

public record FirstUseTransportationDto(
        Transportation transportation, //처음 사용하는 버스(지하철)
        int walkTime //처음 사용할 정류장(역)까지 걷는 시간
) {

    public static FirstUseTransportationDto from(List<Transportation> transportations) {
        int walkTime = 0;

        for (Transportation transportation : transportations) {
            switch (transportation.getTransportationType()) {
                case WALK -> {
                    walkTime += transportation.getTime();
                }
                case BUS -> {
                    Bus bus = (Bus) transportation;
                    return new FirstUseTransportationDto(bus, walkTime);
                }
                case SUBWAY -> {
                    Subway subway = (Subway) transportation;
                    return new FirstUseTransportationDto(subway, walkTime);
                }
            }
        }

        throw new IllegalStateException("Walk Only Route is Not Accepted");
    }
}
