package com.law.tunilmu.authentication.exceptions;

public class TokenBlackListedException extends RuntimeException {
    public TokenBlackListedException(String message) {
        super(message);
    }
}
