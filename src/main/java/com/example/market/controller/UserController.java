package com.example.market.controller;


import com.example.market.dto.create.CartItemCreateDto;
import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.service.UserService;
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
    public List<UserResponseDto> getAllUsers(){
        return userService.getALlUsers();
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }


}
