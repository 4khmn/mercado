package com.example.market.controller;

import com.example.market.controller.ProductController;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;


    @Test
    void getAllProducts_returnListOfProductsResponseDtos() throws Exception {
        ProductResponseDto dto1 = new ProductResponseDto();
        dto1.setId(1L);
        dto1.setTitle("Product 1");

        ProductResponseDto dto2 = new ProductResponseDto();
        dto2.setId(2L);
        dto2.setTitle("Product 2");

        given(productService.getAllProducts())
                .willReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(productService).getAllProducts();
    }
}
