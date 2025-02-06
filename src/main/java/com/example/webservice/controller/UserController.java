package com.example.webservice.controller;

import com.example.webservice.security.JwtValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")

public class UserController {

    @GetMapping("/profile")
    public String getUserProfile(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        if (!JwtValidator.isTokenValid(token, request)) {
            return "Unauthorized: IP address mismatch. Please log in again.";
        }
        return "User profile data (only accessible with valid token and same IP)";
    }
}
