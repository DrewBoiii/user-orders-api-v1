package com.example.demo.service;

import com.example.demo.dto.ProductInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Service
public class ProductInfoService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();

    public Flux<ProductInfoDto> getProductInfo(String productCode) {
        String uri = UriComponentsBuilder.fromUriString("/productInfoService/product/names")
                .queryParam("productCode", productCode)
                .buildAndExpand()
                .toUriString();
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(ProductInfoDto.class)
                .map(productInfoDto -> productInfoDto)
                .log();
    }

}
