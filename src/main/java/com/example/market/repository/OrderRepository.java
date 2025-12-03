package com.example.market.repository;

import com.example.market.model.Order;
import com.example.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {



    List<Order> findByUserId(long id);
}
