package com.tto.ttodomain.transportation.subway;

import java.time.LocalDateTime;
import java.util.List;

public interface SubwayArrivalTimeService {

    List<LocalDateTime> execute(String stationId, String wayCode);
}
