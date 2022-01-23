package com.example.demo.service;

import com.example.demo.dto.OrderSearchDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Service
public class OrderSearchService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8081")
            .build();

    public Flux<OrderSearchDto> getOrderSearch(String phoneNumber) {
        String uri = UriComponentsBuilder.fromUriString("/orderSearchService/order/phone")
                .queryParam("phoneNumber", phoneNumber)
                .buildAndExpand()
                .toUriString();
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(OrderSearchDto.class)
                .log();
    }

}
