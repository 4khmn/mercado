package com.example.market.controller;


import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserCreateDto user){
        log.info("POST /api/users — creating new user with username={}", user.getName());
        UserResponseDto created = userService.createUser(user);
        log.info("User created successfully with id={}", created.getId());
        return created;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponseDto> getAllUsers(){
        log.info("GET /api/users — fetching all users");
        return userService.getALlUsers();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponseDto getUserById(@PathVariable long id){
        log.info("GET /api/users/{} — fetching user", id);
        return userService.getUserById(id);
    }


    @GetMapping("/me")
    public UserResponseDto me() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof String) {
            username = (String) principal;
        } else {
            username = principal.toString();
        }
        log.info("GET /api/me — fetching current user {}", username);
        return userService.getUserByNameDto(username);
    }
}
