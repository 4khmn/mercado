package com.example.market.controller;


import com.example.market.dto.response.ProductResponseDto;
import com.example.market.dto.create.ShopCreateDto;
import com.example.market.dto.response.ShopResponseDto;
import com.example.market.service.ShopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService service) {
        this.shopService = service;
    }


    @PostMapping("/shops")
    public ShopResponseDto createShop(@RequestBody ShopCreateDto shop){
        return shopService.createShop(shop);
    }

    @GetMapping("/shops")
    public List<ShopResponseDto> getAllShops(){
        return shopService.getAllShops();
    }
    @GetMapping("/shops/{id}")
    public ShopResponseDto getShopById(@PathVariable(name = "id") long id){
        return shopService.getShopById(id);
    }

    @GetMapping("/shops/{shopId}/products")
    public List<ProductResponseDto> getProductsByShop(@PathVariable long shopId){
        return shopService.getProductsByShop(shopId);
    }

}
