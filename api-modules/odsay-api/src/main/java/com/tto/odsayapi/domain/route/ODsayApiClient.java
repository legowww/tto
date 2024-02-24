package com.tto.odsayapi.domain.route;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Component
@HttpExchange
public interface ODsayApiClient {

    @GetExchange
    String find(@RequestParam String apiKey, @RequestParam String SX, @RequestParam String SY, @RequestParam String EX, @RequestParam String EY);
}
