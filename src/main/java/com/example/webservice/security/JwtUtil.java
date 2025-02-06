package com.example.webservice.security;

import com.example.webservice.config.DatabaseConfig;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    public String generateToken(String username, String ip, String userAgent, DatabaseConfig databaseConfig) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ip", ip);
        claims.put("userAgent", userAgent);

        Map<String, Object> databaseClaims = new HashMap<>();
        databaseClaims.put("server", databaseConfig.getServer());
        databaseClaims.put("databaseName", databaseConfig.getDatabaseName());
        databaseClaims.put("username", databaseConfig.getUsername());
        databaseClaims.put("password", databaseConfig.getPassword());

        claims.put("database", databaseClaims);

        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Claims decodeToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (ExpiredJwtException e) {
        } catch (SignatureException e) {
        } catch (MalformedJwtException e) {
        } catch (Exception e) {
        }
        return null;
    }
}
