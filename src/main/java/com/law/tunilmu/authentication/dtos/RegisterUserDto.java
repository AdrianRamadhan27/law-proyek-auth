package com.law.tunilmu.authentication.dtos;

import lombok.Getter;

@Getter
public class RegisterUserDto {
    private String email;

    private String password;

    private String username;

    private String role;

    // getters and setters here...
}
