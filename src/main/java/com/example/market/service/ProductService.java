package com.example.market.service;

import com.example.market.dto.create.ProductCreateDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.ProductMapper;
import com.example.market.model.Product;
import com.example.market.model.Shop;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.ShopRepository;
import com.example.market.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final ProductMapper mapper;


    public ProductService(ProductRepository productRepository, ShopRepository shopRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.mapper = mapper;
    }

    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(mapper::toDto);
    }

    public ProductResponseDto createProduct(ProductCreateDto dto){

        Product product = mapper.toEntity(dto);
        Shop shop = shopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new NotFoundException("Shop with id=" + dto.getShopId() + " not found"));
        product.setShop(shop);

        productRepository.save(product);

        return mapper.toDto(product);
    }

    public ProductResponseDto getProductById(long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product with id=" + id + " not found"));
        return mapper.toDto(product);
    }

    public Page<ProductResponseDto> searchProduct(
            String keyword,
            Boolean inStock,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            List<Long> shopId,
            Pageable pageable
    ) {
        Specification<Product> specification = ProductSpecification.searchByKeyword(keyword);
        if (inStock!=null || Boolean.TRUE.equals(inStock)) {
            specification = specification.and(ProductSpecification.hasStock());
        }
        specification = specification
                .and(ProductSpecification.priceFrom(priceFrom))
                .and(ProductSpecification.priceTo(priceTo))
                .and(ProductSpecification.shopId(shopId));
        Page<Product> products = productRepository.findAll(specification, pageable);
        return products.map(mapper::toDto);
    }

}
