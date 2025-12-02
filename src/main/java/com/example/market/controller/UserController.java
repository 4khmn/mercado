package com.example.market.controller;


import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserCreateDto user){
        return userService.createUser(user);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponseDto> getAllUsers(){
        return userService.getALlUsers();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponseDto getUserById(@PathVariable long id){
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

        return userService.getUserByNameDto(username);
    }
}
