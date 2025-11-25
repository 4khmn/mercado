package com.example.market.repository;

import com.example.market.model.Product;
import com.example.market.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop getShopById(long id);

}
