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

    // ✅ JWT Token oluştur
    public static String generateToken(String username, String ip, DatabaseConfig databaseConfig) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            System.out.println("🔐 JWT İçin Şifrelenmeden önce database JSON: " + objectMapper.writeValueAsString(databaseConfig));

            return Jwts.builder()
                    .setSubject(username)
                    .claim("ip", ip)
                    .claim("database", objectMapper.convertValue(databaseConfig, Map.class))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 saat geçerli
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY_BYTES)
                    .compact();
        } catch (Exception e) {
            System.out.println("❌ JWT Token Oluşturma Hatası: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ✅ JWT Token çözme
    public static Claims decodeToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY_BYTES)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("❌ JWT Token Çözme Hatası: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ✅ JWT içinden database bilgisi al
    public static DatabaseConfig getDatabaseConfigFromToken(String token) {
        try {
            token = token.replace("Bearer ", "").trim(); // Bearer ön ekini kaldır

            Claims claims = decodeToken(token);
            if (claims == null) {
                System.out.println("❌ JWT Claims boş!");
                return null;
            }

            Object databaseObject = claims.get("database");
            if (databaseObject == null) {
                System.out.println("❌ JWT içinde database bilgisi bulunamadı!");
                return null;
            }

            System.out.println("🔐 JWT İçinden Gelen Database Nesnesi: " + databaseObject.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            if (databaseObject instanceof String) {
                databaseObject = objectMapper.readValue((String) databaseObject, Map.class);
            }

            DatabaseConfig databaseConfig = objectMapper.convertValue(databaseObject, DatabaseConfig.class);

            System.out.println("✅ JWT İçinden Çözülen Database Config: " + objectMapper.writeValueAsString(databaseConfig));

            return databaseConfig;

        } catch (Exception e) {
            System.out.println("❌ JWT içinden database bilgisi alınamadı: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
