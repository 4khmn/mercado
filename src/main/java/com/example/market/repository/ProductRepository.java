package com.example.market.repository;

import com.example.market.enums.ProductCategory;
import com.example.market.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {



    Page<Product> getProductsByShopId(long id, Pageable pageable);

    Page<Product> getProductsByCategory(ProductCategory category, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category != 'null'")
    List<ProductCategory> findDistinctCategories();
}
