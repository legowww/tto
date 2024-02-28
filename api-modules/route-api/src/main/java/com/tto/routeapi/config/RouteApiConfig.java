package com.tto.routeapi.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ODsayProperties.class)
public class RouteApiConfig {
}
