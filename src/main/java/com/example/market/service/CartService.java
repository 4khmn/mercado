package com.example.market.service;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.CartMapper;
import com.example.market.model.CartItem;
import com.example.market.model.Product;
import com.example.market.model.User;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartService(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository, CartMapper cartMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    public List<CartItemResponseDto> getCartByUser(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        List<CartItemResponseDto> cartItems = user.getCart().stream().map(cartMapper::toDto).toList();
        return cartItems;
    }

    public CartItemResponseDto addCartItem(CartItemCreateDto cartItemDto){
        CartItem cartItem = cartMapper.toEntity(cartItemDto);
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(cartItem);
    }
}
