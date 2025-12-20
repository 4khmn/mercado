package com.example.market.service;

import com.example.market.dto.create.UserCreateDto;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public User addUser(UserCreateDto dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return user;
    }
}
