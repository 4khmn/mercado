package com.example.market.service;

import com.example.market.dto.create.OrderCreateDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.mapper.OrderMapper;
import com.example.market.model.Order;
import com.example.market.model.OrderItem;
import com.example.market.model.Product;
import com.example.market.model.User;
import com.example.market.repository.OrderRepository;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public OrderResponseDto createOrder(OrderCreateDto dto){
        Order order = new Order();
        User user = userRepository.getUserById(dto.getUserId());
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        List<OrderItem> items = dto.getItems().stream().map(itemDto -> {
            Product product = productRepository.getProductById(itemDto.getProductId());
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getPrice());

            return item;

        }).toList();
        order.setItems(items);
        orderRepository.save(order);

        return mapper.toDto(order);
    }

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(mapper::toDto).toList();
    }
    public OrderResponseDto getOrderById(long id){
        Order order = orderRepository.getOrderById(id);
        return mapper.toDto(order);
    }
    public List<OrderResponseDto> getOrdersByUser(long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(mapper::toDto).toList();
    }
}
