package com.example.market.mapper;

import com.example.market.dto.create.ShopCreateDto;
import com.example.market.dto.response.ShopResponseDto;
import com.example.market.model.Shop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopMapper {
    ShopResponseDto toDto(Shop shop);

    Shop toEntity(ShopCreateDto dto);
}
