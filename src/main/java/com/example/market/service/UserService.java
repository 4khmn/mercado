package com.example.market.service;

import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.mapper.UserMapper;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;



    public UserResponseDto createUser(UserCreateDto dto){
        User user = userMapper.toEntity(dto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public List<UserResponseDto> getALlUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDto).toList();
    }

    public UserResponseDto getUserById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + id + " not found"));
        return userMapper.toDto(user);
    }


    public UserResponseDto getUserByNameDto(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        return dto;
    }


}
