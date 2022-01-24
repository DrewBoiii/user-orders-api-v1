package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderSearchService orderSearchService;
    private final ProductInfoService productInfoService;
    private final UserService userService;

    public Flux<OrderDto> getOrders(String userId) {
        return userService.getUser(userId).log()
                .defaultIfEmpty(new User("user777", "CRISTIANOOO RONALDOOOOOO", "88005553535"))
                .flatMapMany(this::getOrderSearchInfo)
                .flatMap(this::enrichWithProductInfo);
    }

    private Flux<OrderDto> getOrderSearchInfo(final User user) {
        final String phoneNumber = user.getPhone();
        return orderSearchService.getOrderSearch(phoneNumber)
                .map(orderSearchDto -> {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setPhoneNumber(phoneNumber);
                    orderDto.setUserName(user.getName());
                    orderDto.setProductCode(orderSearchDto.getProductCode());
                    orderDto.setOrderNumber(orderSearchDto.getOrderNumber());
                    return orderDto;
                });
    }

    private Flux<OrderDto> enrichWithProductInfo(OrderDto orderDto) {
        return productInfoService.getProductInfo(orderDto.getProductCode())
                .map(productInfoDto -> {
                    orderDto.setProductName(productInfoDto.getProductName());
                    orderDto.setProductId(productInfoDto.getProductId());
                    return orderDto;
                });
    }

}
