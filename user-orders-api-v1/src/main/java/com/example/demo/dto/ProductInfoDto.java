package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ProductInfoDto {

    private String productId;
    private String productCode;
    private String productName;
    @JsonAlias("score")
    private Double productScore;

}
