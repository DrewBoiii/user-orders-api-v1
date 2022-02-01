package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("orderSearchWebClient")
    public WebClient orderSearchWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Bean("productInfoWebClient")
    public WebClient productInfoWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

}
