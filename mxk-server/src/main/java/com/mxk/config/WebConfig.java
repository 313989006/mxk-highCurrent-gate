package com.mxk.config;

import com.mxk.filter.PluginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 *
 */
@Configuration
@EnableWebFlux
@EnableConfigurationProperties(ServerConfigProperties.class)
public class WebConfig {

    @org.springframework.context.annotation.Bean
    public PluginFilter pluginFilter(@Autowired ServerConfigProperties properties) {
        return new PluginFilter(properties);
    }
}
