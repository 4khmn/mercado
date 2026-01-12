package com.example.market.controller;


import com.example.market.annotation.GetAllEntities;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.dto.create.ShopCreateDto;
import com.example.market.dto.response.ShopResponseDto;
import com.example.market.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService service) {
        this.shopService = service;
    }

    @PostMapping("/shops")
    public ShopResponseDto createShop(@RequestBody ShopCreateDto shop){
        log.info("POST api/shops - creating new shop with name={}", shop.getName());
        ShopResponseDto created = shopService.createShop(shop);
        log.info("shop created successfully with id={}", created.getId());
        return created;
    }

    @GetAllEntities("Shop")
    @GetMapping("/shops")
    public Page<ShopResponseDto> getAllShops(Pageable pageable){
        return shopService.getAllShops(pageable);
    }
    @GetEntity("Shop")
    @GetMapping("/shops/{id}")
    public ShopResponseDto getShopById(@PathVariable(name = "id") long id){
        return shopService.getShopById(id);
    }

    @GetMapping("/shops/{shopId}/products")
    public Page<ProductResponseDto> getProductsByShop(
            @PathVariable long shopId,
            @PageableDefault(page = 0, size = 50, sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        log.info("GET /api/shops/{}/products - fetching products with shopId={}", shopId, shopId);
        return shopService.getProductsByShop(shopId, pageable);
    }
}
