package com.example.market.service;

import com.example.market.dto.response.ProductResponseDto;
import com.example.market.dto.create.ShopCreateDto;
import com.example.market.dto.response.ShopResponseDto;
import com.example.market.mapper.ProductMapper;
import com.example.market.mapper.ShopMapper;
import com.example.market.model.Product;
import com.example.market.model.Shop;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final ShopMapper shopMapper;
    private final ProductMapper productMapper;

    public ShopService(ShopRepository shopRepository, ProductRepository productRepository, ShopMapper mapper, ProductMapper productMapper) {
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
        this.shopMapper = mapper;
        this.productMapper = productMapper;
    }

    public List<ShopResponseDto> getAllShops(){
        List<Shop> shops = shopRepository.findAll();
        return shops.stream().map(shopMapper::toDto).toList();
    }

    public ShopResponseDto getShopById(long id){
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shop with id=" + id + " not found"));
        return shopMapper.toDto(shop);
    }
    public ShopResponseDto createShop(ShopCreateDto dto){
        Shop shop = shopMapper.toEntity(dto);

        shopRepository.save(shop);
        return shopMapper.toDto(shop);
    }

    public List<ProductResponseDto> getProductsByShop(long shopId){
        List<Product> products = productRepository.getProductsByShopId(shopId);
        return products.stream().map(productMapper::toDto).toList();
    }

}
