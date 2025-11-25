package com.example.market.mapper;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem toEntity(CartItemCreateDto dto);
}
