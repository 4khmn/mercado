package com.example.market.dto.create;

import com.example.market.enums.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateDto {
    private String title;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private long shopId;

    private ProductCategory category;
}
