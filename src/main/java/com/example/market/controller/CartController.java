package com.example.market.controller;

import com.example.market.annotation.GetEntity;
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
        log.info("GET /api/admin/users/{userId}/cart - fetching cart by username with id={}", id, id);
        return cartService.getCartByUser(id);
    }
    @GetMapping("/cart")
    public List<CartItemResponseDto> getCart(@AuthenticationPrincipal User user){
        log.info("GET /api/cart - fetching cart by username={}", user.getUsername());
        return cartService.getCart(user);
    }


    @PostMapping("/products/add")
    public CartItemResponseDto addCartItem(@RequestBody CartItemCreateDto cartItemDto,
                                           @AuthenticationPrincipal User user){
        log.info("POST /api/products/add - add product with id={} for username={}", cartItemDto.getProductId(), user.getUsername());
        CartItemResponseDto cartItemResponseDto = cartService.addCartItem(cartItemDto, user);
        log.info("product with id={} has been added with cartItem id={}", cartItemDto.getProductId(), cartItemResponseDto.getId());
        return cartItemResponseDto;
    }
}
