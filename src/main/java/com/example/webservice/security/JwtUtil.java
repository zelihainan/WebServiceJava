package com.example.webservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "mySecretKeyForJWT";

    // JWT Token oluşturma
    public static String generateToken(String username, String ip) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("ip", ip)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 saat geçerli
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes());

        return builder.compact();
    }

    public static Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT içinden kullanıcı adını al
    public static String getUserFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    // JWT içinden IP adresini al
    public static String getIpFromToken(String token) {
        return (String) decodeToken(token).get("ip");
    }
}