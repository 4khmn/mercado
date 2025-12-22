package com.example.market.repository;

import com.example.market.model.CartItem;
import com.example.market.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> getCartItemByProductIdAndUserId(Long productId, Long userId);

    List<CartItem> getCartItemsByIdInAndUserId(List<Long> ids, Long userId);

}
