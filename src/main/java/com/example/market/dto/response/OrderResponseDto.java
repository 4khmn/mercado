package com.example.market.dto.response;

import com.example.market.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long id;

    private long userId;

    private List<OrderItemResponseDto> items;

    private LocalDateTime createdAt;

    private BigDecimal totalPrice;

    private long totalItemsQuantity;

    private OrderStatus status;
}
