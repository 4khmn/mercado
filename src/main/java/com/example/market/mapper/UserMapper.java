package com.example.market.mapper;

import com.example.market.dto.create.UserCreateDto;
import com.example.market.dto.response.UserResponseDto;
import com.example.market.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);

    User toEntity(UserCreateDto dto);
}
