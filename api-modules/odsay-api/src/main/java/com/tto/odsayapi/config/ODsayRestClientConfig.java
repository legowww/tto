package com.tto.odsayapi.config;


import com.tto.odsayapi.domain.route.ODsayApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ODsayRestClientConfig {

    @Bean
    public ODsayApiClient oDsayApiClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.odsay.com/v1/api/searchPubTransPathT")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(ODsayApiClient.class);
    }
}
