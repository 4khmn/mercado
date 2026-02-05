package com.example.market.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserResponseDto {

    private long id;

    private String username;

    private String email;

    private BigDecimal balance;
}
