package com.example.demo.service;

import com.example.demo.dto.ProductInfoDto;
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
import java.util.Comparator;

@Slf4j
@Service
public class ProductInfoService {

    public static final String BASE_PATH = "/productInfoService/product/names";

    private final WebClient webClient;

    public ProductInfoService(@Qualifier("productInfoWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<ProductInfoDto> getProductInfo(String productCode) {
        String uri = UriComponentsBuilder.fromUriString(BASE_PATH)
                .queryParam("productCode", productCode)
                .buildAndExpand()
                .toUriString();
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    log.error("Request to ProductInfoService was failed - {}", clientResponse);
                    return clientResponse.createException();
                })
                .bodyToFlux(ProductInfoDto.class)
                .sort(Comparator.comparing(ProductInfoDto::getProductScore))
                .takeLast(1)
                .onErrorMap(OrderServiceException::new)
                .doOnError(throwable -> log.error("ProductInfoService Error {}", throwable.getMessage()))
                .retryWhen(Retry.backoff(5L, Duration.ofMillis(100L))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())))
                .log();
    }

}
