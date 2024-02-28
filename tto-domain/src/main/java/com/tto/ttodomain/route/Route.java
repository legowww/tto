package com.tto.ttodomain.route;

import com.tto.ttodomain.transportation.Transportation;

import java.util.List;

public record Route(
        int totalTime, //소요 시간
        String busTransitCount, //버스 탑승 횟수
        String subwayTransitCount, //지하철 탑승 횟수
        String firstStartStation, //탑승 장소
        String lastEndStation, //하차 장소
        List<Transportation> transportations //사용하는 교통수단 리스트
) {

    @Override
    public String toString() {
        return "총시간:" + totalTime +
                "분 출발:" + firstStartStation +
                " 도착:" + lastEndStation +
                " 버스:" + busTransitCount +
                " 지하철:" + subwayTransitCount;
    }
}
