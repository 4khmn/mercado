package com.example.market.controller;


import com.example.market.annotation.CreatedEntity;
import com.example.market.annotation.GetAllEntities;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.dto.create.ShopCreateDto;
import com.example.market.dto.response.ShopResponseDto;
import com.example.market.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService service) {
        this.shopService = service;
    }

    @CreatedEntity("Shop")
    @PostMapping("/shops")
    public ShopResponseDto createShop(@RequestBody ShopCreateDto shop){
        ShopResponseDto created = shopService.createShop(shop);
        return created;
    }

    @GetAllEntities("Shop")
    @GetMapping("/shops")
    public List<ShopResponseDto> getAllShops(){
        return shopService.getAllShops();
    }
    @GetEntity("Shop")
    @GetMapping("/shops/{id}")
    public ShopResponseDto getShopById(@PathVariable(name = "id") long id){
        return shopService.getShopById(id);
    }

    @GetMapping("/shops/{shopId}/products")
    public List<ProductResponseDto> getProductsByShop(@PathVariable long shopId){
        log.info("GET /api/shops/{}/products - fetching products with shopId={}", shopId, shopId);
        return shopService.getProductsByShop(shopId);
    }

}
