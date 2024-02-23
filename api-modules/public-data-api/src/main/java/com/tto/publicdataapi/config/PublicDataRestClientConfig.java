package com.tto.publicdataapi.config;


import com.tto.publicdataapi.domain.bus.PublicDataBusArrivalApiClient;
import com.tto.publicdataapi.domain.subway.PublicDataSubwayArrivalApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class PublicDataRestClientConfig {

    @Bean
    public PublicDataBusArrivalApiClient publicDataBusArrivalApiClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://apis.data.go.kr/6280000/busArrivalService/getBusArrivalList")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(PublicDataBusArrivalApiClient.class);
    }

    @Bean
    public PublicDataSubwayArrivalApiClient publicDataSubwayArrivalApiClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.odsay.com/v1/api/subwayTimeTable")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(PublicDataSubwayArrivalApiClient.class);
    }
}
