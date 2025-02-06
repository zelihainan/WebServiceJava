package com.example.webservice.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;


import java.net.InetAddress;

public class JwtValidator {
    private static final String SECRET_KEY = "mySuperSecretKey";

    public static boolean isTokenValid(String token, HttpServletRequest request) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            // Token içindeki IP adresini al
            String tokenIp = (String) claims.get("ip");

            // Kullanıcının mevcut IP adresini al
            String currentIp = request.getRemoteAddr();
            if (currentIp.equals("0:0:0:0:0:0:0:1")) { // Eğer localhost ise
                currentIp = InetAddress.getLocalHost().getHostAddress();
            }

            // Eğer token içindeki IP ile mevcut IP eşleşmiyorsa, kullanıcı tekrar giriş yapmalı
            return tokenIp.equals(currentIp);
        } catch (Exception e) {
            return false;
        }
    }
}

