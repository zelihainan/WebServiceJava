package com.example.webservice.controller;

import com.example.webservice.service.DatabaseConnectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    private final DatabaseConnectionService databaseConnectionService;

    public DatabaseController(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    @GetMapping("/test")
    public String testDatabaseConnection(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        if (token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "").trim();
        }

        boolean isConnected = databaseConnectionService.testDatabaseConnection(token);

        return isConnected ? "Database connection successful" : "Database connection failed";
    }
}
