package com.example.market.controller;

import com.example.market.annotation.CreatedEntity;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.create.OrderCreateDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.model.User;
import com.example.market.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @CreatedEntity("Order")
    @PostMapping("/orders")
    public OrderResponseDto createOrder(@RequestBody OrderCreateDto order){
        OrderResponseDto created = orderService.createOrder(order);
        return created;
    }

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponseDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> getOrdersByUser(@AuthenticationPrincipal User user){
        return orderService.getOrdersByUser(user.getId());
    }

    @GetEntity("Order")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/orders/{id}")
    public OrderResponseDto getOrderById(@PathVariable long id){
        return orderService.getOrderById(id);
    }

    @GetMapping("/admin/users/{userId}/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponseDto> getOrdersByUser(@PathVariable long userId){
        return orderService.getOrdersByUser(userId);
    }
}
