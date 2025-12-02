package com.example.market.controller;

import com.example.market.config.JwtUtil;
import com.example.market.dto.login.AuthResponse;
import com.example.market.dto.login.LoginRequest;
import com.example.market.model.User;
import com.example.market.service.AuthService;
import com.example.market.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/new-user")
    public String addUser(@RequestBody User user){
        authService.addUser(user);
        return "User added successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));


            org.springframework.security.core.userdetails.UserDetails userDetails =
                    userService.loadUserByUsername(request.getUsername());

            String roles = String.join(", ", userDetails.getAuthorities().stream()
                    .map(a -> a.getAuthority()).toList());

            String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

            return new AuthResponse(token);
    }
}
