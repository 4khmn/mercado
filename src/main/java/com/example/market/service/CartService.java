package com.example.market.service;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.CartResponseDto;
import com.example.market.dto.update.UpdateCartItemQuantity;
import com.example.market.enums.QuantityDirection;
import com.example.market.exception.CartException;
import com.example.market.exception.IllegalQuantityException;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.CartMapper;
import com.example.market.model.CartItem;
import com.example.market.model.Product;
import com.example.market.model.User;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public CartResponseDto getCartByUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        List<CartItemResponseDto> cartItems = user.getCart().stream()
                .map(cartItem -> {
                    CartItemResponseDto dto = cartMapper.toDto(cartItem);
                    long shopId = cartItem.getProduct().getShop().getId();
                    dto.getProduct().setShopId(shopId);
                    return dto;
                })
                .toList();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItemResponseDto cartItem : cartItems) {
            totalPrice = totalPrice.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setCart(cartItems);
        cartResponseDto.setTotalPrice(totalPrice);
        return cartResponseDto;
    }

    @Transactional
    public CartItemResponseDto addCartItem(CartItemCreateDto cartItemDto, User authUser) throws IllegalQuantityException {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new NotFoundException("User with id=" + authUser.getId() + " not found"));
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product with id=" + cartItemDto.getProductId() + " not found"));
        Optional<CartItem> cartItemByProductIdByUserId = cartItemRepository
                .getCartItemByProductIdAndUserId(cartItemDto.getProductId(), user.getId());
        CartItem cartItem;
        long shopId = product.getShop().getId();
        if (cartItemByProductIdByUserId.isPresent()) {
            cartItem = cartItemByProductIdByUserId.get();
            long quantity = cartItem.getQuantity();
            if (product.getStock() > quantity) {
                cartItem.setQuantity(quantity + 1);
            } else {
                throw new IllegalQuantityException("Unable to add product. It's out of stock.");
            }
        } else {
            if (product.getStock() > 0) {
                cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setQuantity(1);
                cartItem.setProduct(product);
                cartItemRepository.save(cartItem);
            } else {
                throw new IllegalQuantityException("Unable to add product. It's out of stock.");
            }
        }
        CartItemResponseDto dto = cartMapper.toDto(cartItem);
        dto.getProduct().setShopId(shopId);
        return dto;
    }

    @Transactional
    public CartItemResponseDto updateQuantity(User authUser, UpdateCartItemQuantity updatedCartItemQuantity) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new NotFoundException("User with id=" + authUser.getId() + " not found"));
        List<CartItem> cart = user.getCart();
        for (CartItem cartItem : cart) {
            if (cartItem.getId() == updatedCartItemQuantity.getCartItemId()) {
                Product product = cartItem.getProduct();
                if (updatedCartItemQuantity.getDirection().equals(QuantityDirection.UP)) {
                    if (product.getStock() > 0) {
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                    } else {
                        throw new IllegalQuantityException("Unable to add product. It's out of stock.");
                    }
                } else {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    if (cartItem.getQuantity() <= 0) {
                        cartItemRepository.deleteById(cartItem.getId());
                    }
                }
                return cartMapper.toDto(cartItem);
            }
        }
        throw new NotFoundException("CartItem not found");
    }

    public CartResponseDto cleanCart(User authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new NotFoundException("User with id=" + authUser.getId() + " not found"));
        if (user.getCart().isEmpty()){
            throw new CartException("Cart is already empty");
        }
        user.getCart().clear();
        CartResponseDto cartResponseDto = new CartResponseDto();
        return cartResponseDto;
    }
}
