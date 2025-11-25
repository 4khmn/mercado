package com.example.market.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long id;

    private UserResponseDto user;

    private List<OrderItemResponseDto> items;

    private LocalDateTime createdAt;
}
