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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
