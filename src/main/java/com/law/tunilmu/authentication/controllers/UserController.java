package com.law.tunilmu.authentication.controllers;

import com.law.tunilmu.authentication.entities.User;
import com.law.tunilmu.authentication.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/emailOf/{name}")
    public ResponseEntity<String> getUserEmailByName(@PathVariable String name) {
    Optional<User> userOptional = userService.getUserByName(name);

    if (userOptional.isPresent()) {
      String email = userOptional.get().getEmail();
      return ResponseEntity.ok(email);
    } else {
      // Handle case where no user is found by name
      return ResponseEntity.notFound().build();
    }
  }
}
