package com.example.market.service;

import com.example.market.dto.create.OrderCreateDto;
import com.example.market.dto.response.OrderItemResponseDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.dto.response.ProductResponseDto;
import com.example.market.exception.CartException;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.OrderItemMapper;
import com.example.market.mapper.OrderMapper;
import com.example.market.mapper.ProductMapper;
import com.example.market.model.*;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.OrderRepository;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderMapper mapper, CartItemRepository cartItemRepository, OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.cartItemRepository = cartItemRepository;
        this.orderMapper = orderMapper;
    }
    @Transactional
    public OrderResponseDto createOrder(User authUser, List<Long> dto){
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new NotFoundException("User with id=" + authUser.getId() + " not found"));
        List<CartItem> cartItems = cartItemRepository.getCartItemsByIdInAndUserId(dto, user.getId());
        if (cartItems.isEmpty()){
            throw new CartException("No cartItems found for order");
        }
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        List<OrderItem> orderItems = new ArrayList<>();
        for (var v: cartItems){
            if (v.getQuantity()<=v.getProduct().getStock()) {
                v.getProduct().setStock((int)(v.getProduct().getStock()-v.getQuantity()));
                OrderItem orderItem = new OrderItem();
                orderItem.setPrice(v.getProduct().getPrice());
                orderItem.setQuantity(v.getQuantity());
                orderItem.setProduct(v.getProduct());
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }
            else{
                throw new CartException("There is not enough stock for order with id=" + v.getProduct().getId());
            }
        }
        order.setItems(orderItems);
        BigDecimal totalPrice = new BigDecimal(0);
        long totalItemsQuantity = 0;
        for (var v: order.getItems()){
            totalPrice = totalPrice.add(v.getPrice().multiply(new BigDecimal(v.getQuantity())));
            totalItemsQuantity = totalItemsQuantity+v.getQuantity();
        }
        order.setTotalPrice(totalPrice);
        order.setTotalItemsQuantity(totalItemsQuantity);
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        OrderResponseDto orderResponseDto = orderMapper.toDto(order);
        for (int i=0; i<orderResponseDto.getItems().size(); i++){
            orderResponseDto.getItems().get(i).setProductId(cartItems.get(i).getProduct().getId());
        }
        orderResponseDto.setUserId(authUser.getId());

        return orderResponseDto;
    }

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(mapper::toDto).toList();
    }
    public OrderResponseDto getOrderById(long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id=" + id + " not found"));
        return mapper.toDto(order);
    }
    public List<OrderResponseDto> getOrdersByUserPathVariable(long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(mapper::toDto).toList();
    }

    public List<OrderResponseDto> getOrdersByUser(User authUser){
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new NotFoundException("User with id=" + authUser.getId() + " not found"));
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream().map(mapper::toDto).toList();
    }
}
