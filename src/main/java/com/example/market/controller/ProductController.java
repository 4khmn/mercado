package com.example.market.controller;

import com.example.market.annotation.GetAllEntities;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.create.ProductCreateDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductCreateDto product){
        log.info("POST /api/products - creating new product with title={}", product.getTitle());
        ProductResponseDto created = productService.createProduct(product);
        log.info("product created successfully with id={}", created.getId());
        return created;
    }

    @GetAllEntities("Product")
    @GetMapping("/products")
    public Page<ProductResponseDto> getAllProducts(
            @PageableDefault(page = 0, size = 50, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetEntity("Product")
    @GetMapping("/products/{id}")
    public ProductResponseDto getProductById(@PathVariable(name = "id") long id){
        return productService.getProductById(id);
    }

}
