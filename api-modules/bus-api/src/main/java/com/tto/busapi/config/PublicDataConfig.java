package com.tto.busapi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PublicDataProperties.class)
public class PublicDataConfig {
}
