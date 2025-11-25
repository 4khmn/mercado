package com.example.market.controller;

import com.example.market.dto.create.OrderCreateDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public OrderResponseDto createOrder(@RequestBody OrderCreateDto order){
        return orderService.createOrder(order);
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> getAllOrders(){
        return orderService.getAllOrders();
    }
    @GetMapping("/orders/{id}")
    public OrderResponseDto getOrderById(@PathVariable long id){
        return orderService.getOrderById(id);
    }

    @GetMapping("/users/{userId}/orders")
    public List<OrderResponseDto> getOrdersByUser(@PathVariable long userId){
        return orderService.getOrdersByUser(userId);
    }
}
