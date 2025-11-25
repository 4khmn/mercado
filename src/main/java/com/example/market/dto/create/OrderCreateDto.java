package com.example.market.dto.create;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDto {

    private long userId;

    private List<OrderItemCreateDto> items;

}
