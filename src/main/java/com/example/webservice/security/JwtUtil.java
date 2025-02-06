package com.example.webservice.security;

import com.example.webservice.config.DatabaseConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_KEY = "mySuperSecretKey";
    private static final byte[] SECRET_KEY_BYTES = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

    public static String generateToken(String username, String ip, DatabaseConfig databaseConfig) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Jwts.builder()
                    .setSubject(username)
                    .claim("ip", ip)
                    .claim("database", objectMapper.convertValue(databaseConfig, Map.class))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY_BYTES)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Claims decodeToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY_BYTES)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DatabaseConfig getDatabaseConfigFromToken(String token) {
        try {
            token = token.replace("Bearer ", "").trim();

            Claims claims = decodeToken(token);
            if (claims == null) {
                return null;
            }

            Object databaseObject = claims.get("database");
            if (databaseObject == null) {
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            if (databaseObject instanceof String) {
                databaseObject = objectMapper.readValue((String) databaseObject, Map.class);
            }

            return objectMapper.convertValue(databaseObject, DatabaseConfig.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
