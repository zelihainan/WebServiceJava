package com.example.webservice.service;

import com.example.webservice.config.DatabaseConfig;
import com.example.webservice.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConnectionService {

    public static boolean testDatabaseConnection(String token) {
        DatabaseConfig config = JwtUtil.getDatabaseConfigFromToken(token);

        if (config == null) {
            return false;
        }
        return connectToDatabase(config);
    }

    private static boolean connectToDatabase(DatabaseConfig config) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}