package com.example.webservice.controller;

import com.example.webservice.service.DatabaseConnectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    @GetMapping("/test")
    public String testDatabaseConnection(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        token = token.replace("Bearer ", "").trim();

        boolean isConnected = DatabaseConnectionService.testDatabaseConnection(token);

        return isConnected ? "Database connection successful" : "Database connection failed";
    }
}
