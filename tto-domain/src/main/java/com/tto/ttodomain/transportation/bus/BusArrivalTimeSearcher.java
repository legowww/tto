package com.tto.ttodomain.transportation.bus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusArrivalTimeSearcher {

    List<LocalDateTime> execute(String bstopId, String routeId);
}
