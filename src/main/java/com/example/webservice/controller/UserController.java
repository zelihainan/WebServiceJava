package com.example.webservice.controller;

import com.example.webservice.security.JwtValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final JwtValidator jwtValidator;

    public UserController(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @GetMapping("/profile")
    public String getUserProfile(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        if (!jwtValidator.isTokenValid(token, request)) {
            return "Unauthorized: IP address mismatch. Please log in again.";
        }
        return "User profile data (only accessible with valid token and same IP)";
    }
}
