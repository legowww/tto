package com.tto.publicdataapi.domain.bus;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Component
@HttpExchange
public interface PublicDataBusArrivalApiClient {

    @GetExchange
    String find(@RequestParam String serviceKey, @RequestParam String pageNo, @RequestParam String numOfRows, @RequestParam String bstopId, @RequestParam String routeId);
}
