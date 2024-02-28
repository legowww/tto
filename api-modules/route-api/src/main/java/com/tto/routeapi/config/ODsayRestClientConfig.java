package com.tto.routeapi.config;


import com.tto.routeapi.domain.route.ODsayRouteSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@RequiredArgsConstructor
@Configuration
public class ODsayRestClientConfig {

    private final ODsayProperties properties;

    @Bean
    public ODsayRouteSearcher oDsayRouteApi() {
        RestClient restClient = RestClient.builder()
                .baseUrl(properties.getUrl())
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(ODsayRouteSearcher.class);
    }
}
