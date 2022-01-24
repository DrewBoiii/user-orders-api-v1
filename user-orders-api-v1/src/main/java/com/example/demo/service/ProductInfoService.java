package com.example.demo.service;

import com.example.demo.dto.ProductInfoDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.util.Comparator;

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
                .bodyToFlux(ProductInfoDto.class)
                .sort(Comparator.comparing(ProductInfoDto::getProductScore))
                .takeLast(1)
                .log();
    }

}
