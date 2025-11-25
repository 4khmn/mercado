package com.example.market.mapper;

import com.example.market.dto.create.OrderItemCreateDto;
import com.example.market.dto.response.OrderItemResponseDto;
import com.example.market.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponseDto toDto(OrderItem orderItem);

    OrderItem toEntity(OrderItemCreateDto dto);
}
