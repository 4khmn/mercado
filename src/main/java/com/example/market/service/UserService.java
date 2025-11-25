package com.example.market.service;

import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.CartItemResponseDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.mapper.CartMapper;
import com.example.market.mapper.UserMapper;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, CartMapper cartMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

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
        User user = userRepository.getUserById(id);
        return userMapper.toDto(user);
    }

}
