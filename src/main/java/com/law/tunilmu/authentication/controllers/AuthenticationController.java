package com.law.tunilmu.authentication.controllers;

import com.law.tunilmu.authentication.entities.User;
import com.law.tunilmu.authentication.responses.LoginResponse;
import com.law.tunilmu.authentication.services.AuthenticationService;
import com.law.tunilmu.authentication.services.JwtService;
import com.law.tunilmu.authentication.dtos.RegisterUserDto;
import com.law.tunilmu.authentication.dtos.LoginUserDto;
import com.law.tunilmu.authentication.services.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final TokenBlacklist tokenBlacklist;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, TokenBlacklist tokenBlacklist) {
        this.jwtService = jwtService;
        this.tokenBlacklist = tokenBlacklist;
        this.authenticationService = authenticationService;

    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        tokenBlacklist.addToBlacklist(token);

        // Clear any session-related data if necessary
        return ResponseEntity.ok("Logged out successfully");
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        // Get the Authorization header from the request
        String authorizationHeader = request.getHeader("Authorization");

        // Check if the Authorization header is not null and starts with "Bearer "
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            // Extract the JWT token (remove "Bearer " prefix)
            return authorizationHeader.substring(7);
        }

        // If the Authorization header is not valid, return null
        return null;
    }


}