package com.example.market.service;

import com.example.market.dto.create.ProductCreateDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.enums.ProductCategory;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.ProductMapper;
import com.example.market.model.Product;
import com.example.market.model.Shop;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {


    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService;

    /// GET ALL PRODUCTS ///
    @Test
    void getAllProductsWithoutCategory_returnsListOfProductResponseDtos(){
        // GIVEN: подготавливаем данные
        Product product1 = new Product();
        Product product2 = new Product();
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> products = List.of(product1, product2);
        ProductResponseDto dto1 = new ProductResponseDto();
        ProductResponseDto dto2 = new ProductResponseDto();
        Page<Product> page = new PageImpl<>(products);
        given(productRepository.findAll(pageable))
                .willReturn(page);

        given(mapper.toDto(product1))
                .willReturn(dto1);

        given(mapper.toDto(product2))
                .willReturn(dto2);

        // WHEN: вызываем метод сервиса
        Page<ProductResponseDto> result = productService.getAllProducts(pageable, null);

        // THEN: проверяем результат
        assertEquals(2, result.getTotalElements());
        assertTrue(result.stream().toList().contains(dto1));
        assertTrue(result.stream().toList().contains(dto2));

        verify(productRepository).findAll(pageable);
    }

    @Test
    void getAllProductsWithCategory_returnsListOfProductResponseDtos(){
        Product product1 = new Product();
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> products = List.of(product1);
        product1.setCategory(ProductCategory.BOOKS);
        ProductResponseDto dto1 = new ProductResponseDto();
        dto1.setCategory(ProductCategory.BOOKS);
        Page<Product> page = new PageImpl<>(products);
        given(productRepository.getProductsByCategory(ProductCategory.BOOKS, pageable))
                .willReturn(page);

        given(mapper.toDto(product1))
                .willReturn(dto1);


        // WHEN: вызываем метод сервиса
        Page<ProductResponseDto> result = productService.getAllProducts(pageable, "books");

        // THEN: проверяем результат
        assertEquals(1, result.getTotalElements());
        assertTrue(result.stream().toList().contains(dto1));

        verify(productRepository).getProductsByCategory(ProductCategory.BOOKS, pageable);
    }
    //////////////////////////

    /// GET PRODUCT BY ID ///
    @Test
    void getProductById_returnProductResponseDto(){
        long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        ProductResponseDto dto = new ProductResponseDto();

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.of(product));
        given(mapper.toDto(product))
                .willReturn(dto);

        ProductResponseDto result = productService.getProductById(productId);

        assertEquals(dto, result);

        verify(productRepository).findById(productId);
    }

    @Test
    void getProductById_returnEntityNotFoundException(){
        long productId = 1L;

        given(productRepository.findById(productId))
            .willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                ()-> productService.getProductById(productId));

        verify(productRepository).findById(productId);

        verify(mapper, never()).toDto(any());
    }
    //////////////////////////

    ///  CREATE PRODUCT ///
    @Test
    void createProduct_returnProductResponseDto(){
        Long shopId = 1L;
        ProductCreateDto createDto = new ProductCreateDto();
        Product product = new Product();

        createDto.setShopId(shopId);

        Shop shop = new Shop();
        ProductResponseDto responseDto = new ProductResponseDto();

        given(mapper.toEntity(createDto))
            .willReturn(product);

        given(shopRepository.findById(createDto.getShopId()))
            .willReturn(Optional.of(shop));

        given(mapper.toDto(product))
            .willReturn(responseDto);


        ProductResponseDto result = productService.createProduct(createDto);


        assertEquals(responseDto, result);
        assertEquals(shop, product.getShop());

        verify(productRepository).save(product);
        verify(shopRepository).findById(shopId);
        verify(mapper).toEntity(createDto);
        verify(mapper).toDto(product);
    }

    @Test
    void updateProduct_returnNotFoundException(){
        Long shopId = 1L;
        ProductCreateDto createDto = new ProductCreateDto();
        Product product = new Product();

        createDto.setShopId(shopId);


        given(mapper.toEntity(createDto))
                .willReturn(product);

        given(shopRepository.findById(createDto.getShopId()))
                .willReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                ()-> productService.createProduct(createDto));


        verify(productRepository, never()).save(any());
        verify(shopRepository).findById(shopId);
        verify(mapper, never()).toDto(any());
    }
    //////////////////////////
}
