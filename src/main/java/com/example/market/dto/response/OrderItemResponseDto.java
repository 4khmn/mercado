package com.example.market.dto.response;


import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderItemResponseDto {

    private Long id;

    private ProductResponseDto product;

    private long quantity;

    private BigDecimal price;
}
