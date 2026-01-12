package com.example.market.service;

import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.OrderItemResponseDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.exception.CartException;
import com.example.market.exception.IllegalQuantityException;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.CartMapper;
import com.example.market.mapper.OrderMapper;
import com.example.market.model.*;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.OrderRepository;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper mapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;

    /// CREATE ORDER ///
    @Test
    void createOrder_successfully_returnOrderResponseDto(){
        Long userId = 1L;
        Long product1Id = 1L;
        Long product2Id = 2L;

        User user = new User();
        user.setId(userId);

        User authUser = new User();
        authUser.setId(userId);

        List<Long> dto = List.of(1L, 2L);

        Product product1 = new Product();
        product1.setId(product1Id);
        product1.setStock(10);
        product1.setPrice(new BigDecimal(1));

        Product product2 = new Product();
        product2.setId(product2Id);
        product2.setStock(10);
        product2.setPrice(new BigDecimal(2));

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setQuantity(1);
        cartItem1.setUser(user);
        cartItem1.setProduct(product1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setQuantity(1);
        cartItem2.setUser(user);
        cartItem2.setProduct(product2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));

        given(cartItemRepository.getCartItemsByIdInAndUserId(dto, user.getId()))
                .willReturn(cartItems);

        OrderResponseDto responseDto = new OrderResponseDto();

        OrderItemResponseDto itemDto1 = new OrderItemResponseDto();
        OrderItemResponseDto itemDto2 = new OrderItemResponseDto();

        responseDto.setItems(List.of(itemDto1, itemDto2));

        given(mapper.toDto(any(Order.class)))
                .willReturn(responseDto);
        OrderResponseDto result =  orderService.createOrder(authUser, dto);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(2, result.getItems().size());
        assertEquals(9, product1.getStock());
        assertEquals(9, product2.getStock());
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        Order order = orderArgumentCaptor.getValue();
        assertEquals(user, order.getUser());
        assertEquals(2, order.getItems().size());

        verify(cartItemRepository).deleteAll(cartItems);
    }

    @Test
    void createOrder_emptyCart_throwCartException(){
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        User authUser = new User();
        authUser.setId(userId);

        List<Long> dto = List.of(1L, 2L);

        List<CartItem> cartItems = new ArrayList<>();

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));

        given(cartItemRepository.getCartItemsByIdInAndUserId(dto, user.getId()))
                .willReturn(cartItems);

        assertThrows(CartException.class,
                () -> orderService.createOrder(authUser, dto));

        verify(orderRepository, never()).save(any());
        verify(cartItemRepository, never()).deleteAll(any());
        verify(mapper, never()).toDto(any());
    }

    @Test
    void createOrder_notEnoughStock_throwCartException(){
        Long userId = 1L;
        Long product1Id = 1L;
        Long product2Id = 2L;

        User user = new User();
        user.setId(userId);

        User authUser = new User();
        authUser.setId(userId);

        List<Long> dto = List.of(1L, 2L);

        Product product1 = new Product();
        product1.setId(product1Id);
        product1.setStock(10);
        product1.setPrice(new BigDecimal(1));

        Product product2 = new Product();
        product2.setId(product2Id);
        product2.setStock(10);
        product2.setPrice(new BigDecimal(2));

        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setQuantity(100);
        cartItem1.setUser(user);
        cartItem1.setProduct(product1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setQuantity(1);
        cartItem2.setUser(user);
        cartItem2.setProduct(product2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        given(userRepository.findById(authUser.getId()))
                .willReturn(Optional.of(user));

        given(cartItemRepository.getCartItemsByIdInAndUserId(dto, user.getId()))
                .willReturn(cartItems);

        assertThrows(CartException.class,
                () -> orderService.createOrder(authUser, dto));

        verify(orderRepository, never()).save(any());
        verify(cartItemRepository, never()).deleteAll(any());
        verify(mapper, never()).toDto(any());
    }
    ///////////////////////

}
