package com.example.market.dto.response;


import lombok.Data;


@Data
public class CartItemResponseDto {
    private long id;

    private ProductResponseDto product;

    private long quantity;


}
