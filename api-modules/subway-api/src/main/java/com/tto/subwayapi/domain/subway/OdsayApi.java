package com.tto.subwayapi.domain.subway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "odsay-subway-api",
        url = "https://api.odsay.com/v1/api/subwayTimeTable"
)
public interface OdsayApi {

    @GetMapping
    String callSubwayTimeTableApi(
            @RequestParam String apiKey,
            @RequestParam String lang,
            @RequestParam String stationID,
            @RequestParam String wayCode
    );
}
