package com.example.market.controller;


import com.example.market.annotation.GetAllEntities;
import com.example.market.annotation.GetEntity;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.dto.update.UpdateBalaceDto;
import com.example.market.dto.update.UpdateEmailDto;
import com.example.market.model.User;
import com.example.market.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public Page<UserResponseDto> getAllUsers(Pageable pageable){
        return userService.getALlUsers(pageable);
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
        log.info("GET /api/profile — fetching current user with username={}", username);
        UserResponseDto currentUser = userService.me(username);
        log.info("Current user with username={} was successfully fetched", username);
        return currentUser;
    }

    @PatchMapping("/edit/balance")
    public UserResponseDto editBalance(@AuthenticationPrincipal User user,
                                       @RequestBody UpdateBalaceDto updateBalaceDto){
        log.info("PATCH /api/edit/balance - updating balance for username={}", user.getUsername());
        UserResponseDto userResponseDto = userService.updateBalance(user.getUsername(), updateBalaceDto);
        log.info("Balance for username={} was successfully updated", user.getUsername());
        return userResponseDto;
    }
    @PatchMapping("/edit/email")
    public UserResponseDto editEmail(@AuthenticationPrincipal User user,
                                     @RequestBody UpdateEmailDto updateEmailDto){
        log.info("PATCH /api/edit/email - updating email for username={}", user.getUsername());
        UserResponseDto userResponseDto = userService.updateEmail(user.getUsername(), updateEmailDto);
        log.info("Email for username={} was successfully updated", user.getUsername());
        return userResponseDto;
    }
}
