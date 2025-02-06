package com.example.webservice.controller;

import com.example.webservice.config.DatabaseConfig;
import com.example.webservice.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestBody DatabaseConfig config, HttpServletRequest request) throws Exception {
        if (config.getUsername() == null || config.getDatabaseName() == null || config.getServer() == null || config.getPassword() == null) {
            return "Error: Database information is missing";
        }

        String ipAddress = request.getRemoteAddr();
        if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        }

        String userAgent = request.getHeader("User-Agent");

        return jwtUtil.generateToken(config.getUsername(), ipAddress, userAgent, config);
    }
}
