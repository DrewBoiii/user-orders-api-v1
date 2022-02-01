package com.example.demo.service;

import com.example.demo.dto.OrderSearchDto;
import com.example.demo.exception.OrderServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
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
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Request to OrderSearchService was failed - {}", clientResponse);
                    return clientResponse.createException();
                })
                .bodyToFlux(OrderSearchDto.class)
                .onErrorMap(OrderServiceException::new)
                .doOnError(throwable -> log.error("OrderSearchService Error {}", throwable.getMessage()))
                .retryWhen(Retry.backoff(5L, Duration.ofMillis(100L))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())))
                .log();
    }

}
