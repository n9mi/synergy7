package com.synergy.binarfood.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("core", r -> r.path("/api/v1/core/**")
                        .uri("http://localhost:8081/api/v1/core/**"))
                .route("core-docs", r -> r.path("/swagger-ui/index.html")
                        .uri("http://localhost:8081/swagger-ui/index.html"))
                .build();
    }
}
