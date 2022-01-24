package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderSearchService orderSearchService;
    private ProductInfoService productInfoService;

    public Flux<OrderDto> getOrders(String userId) {
        return orderSearchService.getOrderSearch("987654321")
                .map(orderSearchDto -> {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setPhoneNumber("987654321");
                    orderDto.setProductCode(orderSearchDto.getProductCode());
                    orderDto.setOrderNumber(orderSearchDto.getOrderNumber());
                    return orderDto;
                })
                .flatMap(this::enrichWithProductInfo);
    }

    private Flux<OrderDto> enrichWithProductInfo(OrderDto orderDto) {
        return productInfoService.getProductInfo(orderDto.getProductCode())
                .map(productInfoDto -> {
                    orderDto.setProductName(productInfoDto.getProductName());
                    orderDto.setProductId(productInfoDto.getProductId());
                    Double productScore = productInfoDto.getProductScore();
                    return orderDto;
                });
    }

}
