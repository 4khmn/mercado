package com.example.market.dto.response;

import com.example.market.model.Product;
import com.example.market.model.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDto {
    private long id;

    private ProductResponseDto product;

    private long quantity;


    private BigDecimal price;

}
