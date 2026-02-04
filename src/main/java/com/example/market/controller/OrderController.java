package com.example.market.controller;

import com.example.market.annotation.GetEntity;
import com.example.market.dto.create.OrderCreateDto;
import com.example.market.dto.response.OrderResponseDto;
import com.example.market.model.User;
import com.example.market.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/orders")
    public OrderResponseDto createOrder(@AuthenticationPrincipal User user,
                                        @RequestBody OrderCreateDto request){
        log.info("POST /api/orders - creating order for user with id={} with cartItemIds={} ", user.getId(), request.getCartItemIds());
        OrderResponseDto created = orderService.createOrder(user, request.getCartItemIds());
        log.info("order created successfully with id={}", created.getId());
        return created;
    }

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderResponseDto> getAllOrders(Pageable pageable){
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/orders")
    public Page<OrderResponseDto> getOrdersByUser(@AuthenticationPrincipal User user, Pageable pageable){
        return orderService.getOrdersByUser(user, pageable);
    }

    @GetEntity("Order")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/orders/{id}")
    public OrderResponseDto getOrderById(@PathVariable long id){
        return orderService.getOrderById(id);
    }

    @GetMapping("/admin/users/{userId}/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderResponseDto> getOrdersByUserPathVariable(@PathVariable long userId, Pageable pageable){
        return orderService.getOrdersByUserPathVariable(userId, pageable);
    }
}
