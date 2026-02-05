package com.example.market.controller;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.CartResponseDto;
import com.example.market.dto.update.UpdateCartItemQuantity;
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
    public CartResponseDto getCartByUser(@PathVariable long id){
        log.info("GET /api/admin/users/{userId}/cart - fetching cart by username with id={}", id, id);
        CartResponseDto cartByUser = cartService.getCartByUser(id);
        log.info("Cart by username with id={} was successfully fetched", id);
        return cartByUser;
    }
    @GetMapping("/cart")
    public CartResponseDto getCart(@AuthenticationPrincipal User user){
        log.info("GET /api/cart - fetching cart by username={}", user.getUsername());
        CartResponseDto cart = cartService.getCartByUser(user.getId());
        log.info("Cart by username={} was successfully fetched", user.getUsername());
        return cart;
    }

    @PatchMapping("/cart")
    public CartItemResponseDto changeCartItemQuantity(@RequestBody UpdateCartItemQuantity updatedCartItemQuantity,
                                                      @AuthenticationPrincipal User user){
        log.info("POST /api/cart - updating information about cartItem with id={} for username={}",
                updatedCartItemQuantity.getCartItemId(), user.getUsername());
        CartItemResponseDto cartItemResponseDto = cartService.updateQuantity(user, updatedCartItemQuantity);
        log.info("cartItem with id={} has been updated", updatedCartItemQuantity.getCartItemId());
        return cartItemResponseDto;

    }

    @PostMapping("/products/add")
    public CartItemResponseDto addCartItem(@RequestBody CartItemCreateDto cartItemDto,
                                           @AuthenticationPrincipal User user){
        log.info("POST /api/products/add - add product with id={} for username={}", cartItemDto.getProductId(), user.getUsername());
        CartItemResponseDto cartItemResponseDto = cartService.addCartItem(cartItemDto, user);
        log.info("product with id={} has been added with cartItem id={}", cartItemDto.getProductId(), cartItemResponseDto.getId());
        return cartItemResponseDto;
    }


    @DeleteMapping("/clean-cart")
    public CartResponseDto cleanCartItem(@AuthenticationPrincipal User user){
        log.info("DELETE /api/products/clean-cart - clean cart for username={}", user.getUsername());
        CartResponseDto cart = cartService.cleanCart(user);
        log.info("Cart has been cleaned successfully for username={}", user.getUsername());
        return cart;
    }
}
