package com.chatapp.controller;

import com.chatapp.dto.AuthResponse;
import com.chatapp.dto.LoginRequest;
import com.chatapp.dto.SignupRequest;
import com.chatapp.model.User;
import com.chatapp.security.JwtUtil;
import com.chatapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://client-spring-boot.vercel.app")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public AuthResponse signup(
            @RequestBody SignupRequest request
    ) {

        User user = authService.signup(request);

        String token = jwtUtil.generateToken(user.getId());

        return AuthResponse.builder()
                .success(true)
                .token(token)
                .user(user)
                .message("Signup successful")
                .build();
    }

//    @PostMapping("/login")
//    public AuthResponse login(
//            @RequestBody LoginRequest request,
//            HttpServletResponse response
//    ) {
//
//        User user = authService.login(request);
//
//        String token =
//                jwtUtil.generateToken(user.getId());
//
//        Cookie cookie =
//                new Cookie("chat-token", token);
//
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(15 * 24 * 60 * 60);
//
//        response.addCookie(cookie);
//
//        return AuthResponse.builder()
//                .success(true)
//                .user(user)
//                .message("Login successful")
//                .build();
//    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        User user = authService.login(request);

        String token = jwtUtil.generateToken(user.getId());

        ResponseCookie cookie = ResponseCookie
                .from("chat-token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(15 * 24 * 60 * 60)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return AuthResponse.builder()
                .success(true)
                .user(user)
                .message("Login successful")
                .build();
    }

//    @GetMapping("/logout")
//    public AuthResponse logout(
//            HttpServletResponse response
//    ) {
//
//        Cookie cookie =
//                new Cookie("chat-token", "");
//
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//
//        response.addCookie(cookie);
//
//        return AuthResponse.builder()
//                .success(true)
//                .message("Logged out successfully")
//                .build();
//    }

    @GetMapping("/logout")
    public AuthResponse logout(
            HttpServletResponse response
    ) {

        ResponseCookie cookie = ResponseCookie
                .from("chat-token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return AuthResponse.builder()
                .success(true)
                .message("Logged out successfully")
                .build();
    }

}