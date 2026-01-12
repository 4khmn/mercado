package com.example.market.controller;

import com.example.market.annotation.CreatedEntity;
import com.example.market.annotation.GetAllEntities;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.create.ProductCreateDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @CreatedEntity("Product")
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductCreateDto product){
        return productService.createProduct(product);
    }

    @GetAllEntities("Product")
    @GetMapping("/products")
    public List<ProductResponseDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetEntity("Product")
    @GetMapping("/products/{id}")
    public ProductResponseDto getProductById(@PathVariable(name = "id") long id){
        return productService.getProductById(id);
    }

}
