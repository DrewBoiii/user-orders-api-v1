package com.example.demo.service;

import com.example.demo.dto.OrderSearchDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Service
public class OrderSearchService {

    public static final String BASE_PATH = "/orderSearchService/order/phone";

    private final WebClient webClient;

    public OrderSearchService(@Qualifier("orderSearchWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<OrderSearchDto> getOrderSearch(String phoneNumber) {
        String uri = UriComponentsBuilder.fromUriString(BASE_PATH)
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
