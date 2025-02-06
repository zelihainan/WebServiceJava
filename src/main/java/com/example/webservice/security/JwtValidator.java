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

            String tokenIp = (String) claims.get("ip");

            String currentIp = request.getRemoteAddr();
            if (currentIp.equals("0:0:0:0:0:0:0:1")) { // Eğer localhost ise
                currentIp = InetAddress.getLocalHost().getHostAddress();
            }

            return tokenIp.equals(currentIp);
        } catch (Exception e) {
            return false;
        }
    }
}

