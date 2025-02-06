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
            if (claims == null) {
                return false;
            }

            String tokenIp = claims.get("ip", String.class);
            String tokenUserAgent = claims.get("userAgent", String.class);

            // Yeni metodu kullanarak gerçek IP'yi alalım
            String currentIp = getClientIp(request);
            String requestUserAgent = request.getHeader("User-Agent");

            return tokenIp.equals(currentIp) && tokenUserAgent.equals(requestUserAgent);
        } catch (Exception e) {
            return false;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                ip = "127.0.0.1";
            }
        }
        return ip;
    }
}
