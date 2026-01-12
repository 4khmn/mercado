package com.example.market.specification;

import com.example.market.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> searchByKeyword(String keyword){
        return ((root, query, cb) -> {
            if (keyword==null || keyword.isBlank()){
                return cb.conjunction();
            }

            String pattern = "%"+keyword.toLowerCase()+"%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        });
    }

    public static Specification<Product> hasStock() {
        return (root, query, cb) ->
                cb.greaterThan(root.get("stock"), 0);
    }

    public static Specification<Product> priceFrom(BigDecimal price) {
        return (root, query, cb) -> {
            if (price == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Product> priceTo(BigDecimal price) {
        return (root, query, cb) -> {
            if (price == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Product> shopId(List<Long> shopId) {
        return (root, query, cb) -> {
            if (shopId == null || shopId.isEmpty()) {
                return null;
            }
            return root.get("shop").get("id").in(shopId);
        };
    }
}
