package com.example.market.mapper;

import com.example.market.dto.create.OrderCreateDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, UserMapper.class, ProductMapper.class, ShopMapper.class})
public interface OrderMapper {

    OrderResponseDto toDto(Order order);

    Order toEntity(OrderCreateDto dto);
}
