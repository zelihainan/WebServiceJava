package com.example.webservice.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class JwtValidator {

    private final JwtUtil jwtUtil;

    public JwtValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean isTokenValid(String token, HttpServletRequest request) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "").trim();
            }

            Claims claims = jwtUtil.decodeToken(token);
            String tokenIp = claims.get("ip", String.class);
            String tokenUserAgent = claims.get("userAgent", String.class);

            String currentIp = request.getRemoteAddr();
            if (currentIp.equals("0:0:0:0:0:0:0:1")) {
                currentIp = InetAddress.getLocalHost().getHostAddress();
            }

            String requestUserAgent = request.getHeader("User-Agent");

            return tokenIp.equals(currentIp) && tokenUserAgent.equals(requestUserAgent);
        } catch (Exception e) {
            return false;
        }
    }
}
