package com.tto.subwayapi.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "odsay")
public class ODsayProperties {

    private final Subway subway;

    @Getter
    @RequiredArgsConstructor
    public static class Subway {
        private final String url;
        private final String key;
    }
}
