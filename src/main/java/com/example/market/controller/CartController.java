package com.example.market.controller;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/{id}")
    public List<CartItemResponseDto> getCartByUser(@PathVariable long id){
        return cartService.getCartByUser(id);
    }


    @PostMapping("/cart/add")
    public CartItemResponseDto addCartItem(@RequestBody CartItemCreateDto cartItemDto){
        return cartService.addCartItem(cartItemDto);
    }

}
