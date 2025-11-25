package com.example.market.repository;

import com.example.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    Product getProductById(long id);

    List<Product> getProductsByShopId(long id);
}
