package com.example.market.service;

import com.example.market.config.MyUserDetails;
import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.mapper.CartMapper;
import com.example.market.mapper.UserMapper;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.getUserByName(username);
        return user.map(MyUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException(username + " not found"));
    }

    public UserResponseDto getUserByNameDto(String username) {
        Optional<User> userOpt = userRepository.getUserByName(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return userMapper.toDto(userOpt.get());
    }
}
