package com.example.webservice.model;

import lombok.Data;

@Data
public class DatabaseConfig {
    private String server;
    private String username;
    private String password;
    private String databaseName;
}
