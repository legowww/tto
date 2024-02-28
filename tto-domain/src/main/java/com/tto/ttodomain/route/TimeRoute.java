package com.tto.ttodomain.route;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TimeRoute(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime timeToOut,
        Route route
) {
}
