package com.example.webservice.controller;

import com.example.webservice.config.DatabaseConfig;
import com.example.webservice.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody DatabaseConfig config, HttpServletRequest request) throws Exception {
        // Kullanıcının IP adresini al
        String ipAddress = request.getRemoteAddr();
        if (ipAddress.equals("0:0:0:0:0:0:0:1")) { // Eğer localhost ise gerçek IP’yi al
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        }

        // JWT token oluştur
        return JwtUtil.generateToken(config.getUsername(), ipAddress);
    }
}
