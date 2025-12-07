package com.example.market.controller;


import com.example.market.annotation.CreatedEntity;
import com.example.market.annotation.GetAllEntities;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.create.UserCreateDto;
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

    @CreatedEntity("User")
    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserCreateDto user){
        UserResponseDto created = userService.createUser(user);
        return created;
    }

    @GetAllEntities("User")
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponseDto> getAllUsers(){
        return userService.getALlUsers();
    }

    @GetMapping("/users/{id}")
    @GetEntity("User")
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
        log.info("GET /api/me — fetching current user {}", username);
        return userService.getUserByNameDto(username);
    }
}
