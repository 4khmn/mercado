package com.example.market.repository;

import com.example.market.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {



    Page<Product> getProductsByShopId(long id, Pageable pageable);
}
