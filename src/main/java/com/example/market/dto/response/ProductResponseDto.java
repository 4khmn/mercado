package com.example.market.dto.response;

import com.example.market.enums.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductResponseDto {

    private long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private long shopId;

    private ProductCategory category;
}
