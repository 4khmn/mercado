package com.example.market.mapper;

import com.example.market.dto.create.ProductCreateDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "shop.id", target = "shopId")
    ProductResponseDto toDto(Product product);

    Product toEntity(ProductCreateDto dto);

}
