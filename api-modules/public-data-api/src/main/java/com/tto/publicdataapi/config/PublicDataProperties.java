package com.tto.publicdataapi.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "public-data")
public class PublicDataProperties {

    private final Bus bus;
    private final Subway subway;

    @Getter
    @RequiredArgsConstructor
    public static class Bus {
        private final String url;
        private final String key;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Subway {
        private final String url;
        private final String key;
    }
}
