package com.tto.ttodomain.transportation.bus;

public record BusArrivalTimeResponse(
        String latestStopId, //최근 버스가 떠난 정류장ID
        int arrivalEstimateTime //내가 탑승할 버스 정류장 도착까지 남은 시간(초)
) {
}
