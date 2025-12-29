package com.example.market.controller;


import com.example.market.annotation.GetAllEntities;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.dto.update.UpdateEmailDto;
import com.example.market.model.User;
import com.example.market.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @GetAllEntities("User")
    @GetMapping("/admin/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponseDto> getAllUsers(){
        return userService.getALlUsers();
    }

    @GetMapping("/admin/users/{id}")
    @GetEntity("User")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponseDto getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }


    @GetMapping("/profile")
    public UserResponseDto me(@AuthenticationPrincipal User user) {
        String username = user.getUsername();
        log.info("GET /api/me — fetching current user {}", username);
        return userService.me(username);
    }

    @PutMapping("/edit/email")
    public UserResponseDto editEmail(@AuthenticationPrincipal User user,
                                     @RequestBody UpdateEmailDto updateEmailDto){
        return userService.updateEmail(user.getUsername(), updateEmailDto);
    }
}
