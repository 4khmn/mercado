package com.example.market.controller;

import com.example.market.config.security.JwtUtil;
import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.login.AuthResponse;
import com.example.market.dto.login.LoginRequest;
import com.example.market.model.User;
import com.example.market.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public String addUser(@RequestBody UserCreateDto dto){
        log.info("POST /api/register — user with username={} trying to register", dto.getUsername());
        User user = authService.addUser(dto);
        log.info("User created successfully with id={}", user.getId());
        return "User added successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(
                userDetails.getUsername(),
                String.join(", ", userDetails.getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .toList())
        );

        return new AuthResponse(token);
    }
}
