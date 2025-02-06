package com.example.webservice.service;

import com.example.webservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DatabaseConnectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionService.class);
    private final JwtUtil jwtUtil;

    // Kullanıcı oturumlarına göre database bilgilerini saklamak için
    private static final Map<String, String[]> databaseCache = new ConcurrentHashMap<>();

    public DatabaseConnectionService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void saveDatabaseConfig(String token, String server, String databaseName, String username, String password) {
        databaseCache.put(token, new String[]{server, databaseName, username, password});
    }

    public boolean testDatabaseConnection(String token) {
        try {
            // Önce cache'de database bilgileri var mı kontrol et
            String[] dbConfig = databaseCache.get(token);
            if (dbConfig == null) {
                LOGGER.error("Database configuration not found for token: {}", token);
                return false;
            }

            String server = dbConfig[0];
            String databaseName = dbConfig[1];
            String username = dbConfig[2];
            String password = dbConfig[3];

            String dbUrl = "jdbc:sqlserver://" + server + ";databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true;loginTimeout=30;";
            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
                return connection.isValid(2);
            }
        } catch (Exception e) {
            LOGGER.error("Database connection error: {}", e.getMessage());
            return false;
        }
    }
}
