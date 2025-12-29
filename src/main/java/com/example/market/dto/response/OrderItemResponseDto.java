package com.example.market.dto.response;


import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderItemResponseDto {

    private Long id;

    private long productId;

    private long quantity;

    private BigDecimal price;
}
