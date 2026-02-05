package com.example.market.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class CartResponseDto {

    private List<CartItemResponseDto> cart;

    private BigDecimal totalPrice;

    {
        totalPrice = BigDecimal.ZERO;
        cart = new ArrayList<>();
    }


}
