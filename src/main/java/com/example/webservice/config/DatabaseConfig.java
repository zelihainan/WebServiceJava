package com.example.webservice.config;

import lombok.Data;

@Data
public class DatabaseConfig {
    private String server;
    private String username;
    private String password;
    private String databaseName;

    public String getUsername() {
        return username;
    }
}
