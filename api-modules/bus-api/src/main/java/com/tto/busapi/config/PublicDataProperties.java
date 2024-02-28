package com.tto.busapi.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "public-data")
public class PublicDataProperties {

    private final Bus bus;

    @Getter
    @RequiredArgsConstructor
    public static class Bus {
        private final String url;
        private final String key;
        private final String pageNo;
        private final String numOfRows;
    }
}
