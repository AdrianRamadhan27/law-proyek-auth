package com.law.tunilmu.authentication.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
public class LoginResponse {
    @Getter
    private String token;

    private long expiresIn;

    // Getters and setters...
}
