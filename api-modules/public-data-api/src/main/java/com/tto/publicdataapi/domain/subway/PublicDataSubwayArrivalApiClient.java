package com.tto.publicdataapi.domain.subway;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Component
@HttpExchange
public interface PublicDataSubwayArrivalApiClient {

    @GetExchange
    String find(@RequestParam String apiKey, @RequestParam String SX, @RequestParam String SY, @RequestParam String EX, @RequestParam String EY);
}
