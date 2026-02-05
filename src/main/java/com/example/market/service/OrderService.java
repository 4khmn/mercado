package com.example.market.service;

import com.example.market.dto.response.OrderResponseDto;
import com.example.market.enums.OrderStatus;
import com.example.market.exception.CartException;
import com.example.market.exception.NotEnoughMoneyException;
import com.example.market.exception.NotFoundException;
import com.example.market.mapper.OrderMapper;
import com.example.market.model.*;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.OrderRepository;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper mapper;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderResponseDto createOrder(User authUser, List<Long> dto){
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new NotFoundException("User with id=" + authUser.getId() + " not found"));
        List<CartItem> cartItems = cartItemRepository.getCartItemsByIdInAndUserId(dto, user.getId());
        if (cartItems.isEmpty()){
            throw new CartException("No cartItems found for order");
        }
        Order order = new Order();
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
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
        if (user.getBalance().compareTo(totalPrice)==-1){
            order.setStatus(OrderStatus.CANCELLED);
            throw new NotEnoughMoneyException("Not enough money");
        } else {
            user.setBalance(user.getBalance().subtract(totalPrice));
            order.setStatus(OrderStatus.PAID);
        }
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        OrderResponseDto orderResponseDto = mapper.toDto(order);
        for (int i=0; i<orderResponseDto.getItems().size(); i++){
            orderResponseDto.getItems().get(i).setProductId(cartItems.get(i).getProduct().getId());
        }
        orderResponseDto.setUserId(authUser.getId());

        return orderResponseDto;
    }

    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(mapper::toDto);
    }
    public OrderResponseDto getOrderById(long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id=" + id + " not found"));
        return mapper.toDto(order);
    }
    public Page<OrderResponseDto> getOrdersByUserPathVariable(long userId, Pageable pageable){
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(mapper::toDto);
    }

    public Page<OrderResponseDto> getOrdersByUser(User authUser, Pageable pageable){
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new NotFoundException("User with id=" + authUser.getId() + " not found"));
        Page<Order> orders = orderRepository.findByUserId(user.getId(), pageable);
        return orders.map(mapper::toDto);
    }
}
