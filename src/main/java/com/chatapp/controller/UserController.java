package com.chatapp.controller;

import com.chatapp.dto.AuthResponse;
import com.chatapp.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/me")
    public Object getMyProfile(
            Authentication authentication
    ) {

        User user =
                (User) authentication.getPrincipal();

        return new Object() {
            public final boolean success = true;
            public final User userData = user;
        };
    }

    @GetMapping("/all")
    public AuthResponse getAllUsers(
            Authentication authentication
    ) {

        User currentUser =
                (User) authentication.getPrincipal();

        List<User> users = userRepository
                .findAll()
                .stream()
                .filter(user ->
                        !user.getId().equals(currentUser.getId()))
                .toList();

        return AuthResponse.builder()
                .success(true)
                .user(users)
                .message("Users fetched successfully")
                .build();
    }
}