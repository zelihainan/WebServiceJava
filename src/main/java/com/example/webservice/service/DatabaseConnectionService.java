package com.example.webservice.service;

import com.example.webservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

@Service
public class DatabaseConnectionService {

    private final JwtUtil jwtUtil;

    public DatabaseConnectionService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean testDatabaseConnection(String token) {
        try {
            Claims claims = jwtUtil.decodeToken(token);
            if (claims == null) {
                return false;
            }

            Object databaseObject = claims.get("database");
            if (databaseObject == null) {
                return false;
            }

            Map<String, Object> databaseClaims = (Map<String, Object>) databaseObject;
            String server = (String) databaseClaims.get("server");
            String databaseName = (String) databaseClaims.get("databaseName");
            String username = (String) databaseClaims.get("username");
            String password = (String) databaseClaims.get("password");

            if (server == null || databaseName == null || username == null || password == null) {
                return false;
            }

            String dbUrl = "jdbc:sqlserver://" + server + ";databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true;loginTimeout=30;";

            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
                if (connection.isValid(2)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
