package com.example.market.service;

import com.example.market.dto.create.ProductCreateDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.mapper.ProductMapper;
import com.example.market.model.Product;
import com.example.market.model.Shop;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.ShopRepository;
import org.springframework.stereotype.Service;

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

    public List<ProductResponseDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(mapper::toDto).toList();
    }

    public ProductResponseDto createProduct(ProductCreateDto dto){

        Product product = mapper.toEntity(dto);
        Shop shop = shopRepository.getShopById(dto.getShopId());
        product.setShop(shop);

        productRepository.save(product);

        return mapper.toDto(product);
    }

    public ProductResponseDto getProductById(long id){
        Product product = productRepository.getProductById(id);
        return mapper.toDto(product);
    }




}
