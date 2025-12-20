package com.example.market.controller;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.model.User;
import com.example.market.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/users/{userId}/cart")
    public List<CartItemResponseDto> getCartByUser(@PathVariable long id){
        return cartService.getCartByUser(id);
    }

    @GetMapping("/cart")
    public List<CartItemResponseDto> getCart(@AuthenticationPrincipal User user){
        return cartService.getCart(user);
    }


    @PostMapping("/products/add")
    public CartItemResponseDto addCartItem(@RequestBody CartItemCreateDto cartItemDto,
                                           @AuthenticationPrincipal User user){
        return cartService.addCartItem(cartItemDto, user);
    }

}
